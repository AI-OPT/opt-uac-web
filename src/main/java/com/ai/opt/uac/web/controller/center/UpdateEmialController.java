package com.ai.opt.uac.web.controller.center;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ai.opt.base.vo.BaseResponse;
import com.ai.opt.base.vo.ResponseHeader;
import com.ai.opt.sdk.cache.factory.CacheClientFactory;
import com.ai.opt.sdk.util.DubboConsumerFactory;
import com.ai.opt.sdk.util.RandomUtil;
import com.ai.opt.sdk.util.UUIDUtil;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.sso.client.filter.SSOClientConstants;
import com.ai.opt.sso.client.filter.SSOClientUser;
import com.ai.opt.uac.api.security.interfaces.IAccountSecurityManageSV;
import com.ai.opt.uac.api.security.param.AccountEmailRequest;
import com.ai.opt.uac.web.constants.Constants;
import com.ai.opt.uac.web.constants.Constants.ResultCode;
import com.ai.opt.uac.web.constants.Constants.UpdateEmail;
import com.ai.opt.uac.web.constants.VerifyConstants.EmailVerifyConstants;
import com.ai.opt.uac.web.constants.VerifyConstants.PhoneVerifyConstants;
import com.ai.opt.uac.web.model.email.SendEmailRequest;
import com.ai.opt.uac.web.model.retakepassword.AccountData;
import com.ai.opt.uac.web.model.retakepassword.SafetyConfirmData;
import com.ai.opt.uac.web.model.retakepassword.SendVerifyRequest;
import com.ai.opt.uac.web.util.CacheUtil;
import com.ai.opt.uac.web.util.VerifyUtil;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;
import com.ai.runner.center.mmp.api.manager.param.SMData;
import com.ai.runner.center.mmp.api.manager.param.SMDataInfoNotify;

@RequestMapping("/center/email")
@Controller
public class UpdateEmialController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateEmialController.class);

	@RequestMapping("/confirminfo")
	public ModelAndView updateEmailStart(HttpServletRequest request) {
		SSOClientUser userClient = (SSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
		if (userClient != null) {
			Map<String, AccountData> model = new HashMap<String, AccountData>();
			String phone = userClient.getPhone();
			String email = userClient.getEmail();
			AccountData confirmInfo = new AccountData(phone, email);
			model.put("confirmInfo", confirmInfo);
			return new ModelAndView("jsp/center/update-email-start", model);
		} else {
			return new ModelAndView("jsp/center/update-email-start");
		}
	}

	@RequestMapping("/getImageVerifyCode")
	@ResponseBody
	public void getImageVerifyCode(HttpServletRequest request, HttpServletResponse response) {
		String cacheKey = UpdateEmail.CACHE_KEY_VERIFY_PICTURE + request.getSession().getId();
		BufferedImage image = VerifyUtil.getImageVerifyCode(request, UpdateEmail.CACHE_NAMESPACE, cacheKey);
		try {
			ImageIO.write(image, "PNG", response.getOutputStream());
		} catch (IOException e) {
			LOGGER.error("生成图片验证码错误：" + e);
			e.printStackTrace();
		}
	}

	/**
	 * 发送验证码
	 * 
	 * @return
	 */
	@RequestMapping("/sendVerify")
	@ResponseBody
	public ResponseData<String> sendVerify(HttpServletRequest request, SendVerifyRequest sendVerifyRequest) {
		SSOClientUser userClient = (SSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
		String checkType = sendVerifyRequest.getCheckType();
		ResponseData<String> responseData = null;
		String sessionId = request.getSession().getId();
		if (userClient != null) {
			if (UpdateEmail.CHECK_TYPE_PHONE.equals(checkType)) {
				// 发送手机验证码
				boolean isSuccess = sendPhoneVerifyCode(sessionId, userClient);
				if (isSuccess) {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "短信验证码发送成功", "短信验证码发送成功");
				} else {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "短信验证码发送失败", "服务器连接超时");
				}
			} else if (UpdateEmail.CHECK_TYPE_EMAIL.equals(checkType)) {
				// 发送邮件验证码
				boolean isSuccess = sendEmailVerifyCode(sessionId, userClient);
				if (isSuccess) {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "邮件验证码发送成功", "邮件验证码发送成功");
				} else {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "邮件验证码发送失败", "服务器连接超时");
				}
			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "验证码发送失败", "验证方式不正确");
			}
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "验证码发送失败", "该账号不存在");
		}
		return responseData;
	}

	/**
	 * 发送手机验证码
	 * 
	 * @param userClient
	 */
	private boolean sendPhoneVerifyCode(String sessionId, SSOClientUser userClient) {
		SMDataInfoNotify smDataInfoNotify = new SMDataInfoNotify();
		String phoneVerifyCode = RandomUtil.randomNum(PhoneVerifyConstants.VERIFY_SIZE);
		// 将验证码放入缓存
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(UpdateEmail.CACHE_NAMESPACE);
		String cacheKey = UpdateEmail.CACHE_KEY_VERIFY_PHONE + sessionId;
		cacheClient.setex(cacheKey, PhoneVerifyConstants.VERIFY_OVERTIME, phoneVerifyCode);
		// 设置短息信息
		List<SMData> dataList = new LinkedList<SMData>();
		SMData smData = new SMData();
		smData.setGsmContent("${VERIFY}:" + phoneVerifyCode + "^${VALIDMINS}:" + PhoneVerifyConstants.VERIFY_OVERTIME / 60);
		smData.setPhone(userClient.getPhone());
		smData.setTemplateId(PhoneVerifyConstants.TEMPLATE_RETAKE_PASSWORD_ID);
		smData.setServiceType(PhoneVerifyConstants.SERVICE_TYPE);
		dataList.add(smData);
		smDataInfoNotify.setDataList(dataList);
		smDataInfoNotify.setMsgSeq(VerifyUtil.createPhoneMsgSeq());
		smDataInfoNotify.setTenantId(userClient.getTenantId());
		smDataInfoNotify.setSystemId(Constants.SYSTEM_ID);
		return VerifyUtil.sendPhoneInfo(smDataInfoNotify);
	}

	/**
	 * 发送邮件验证码
	 * 
	 * @param accountInfo
	 */
	private boolean sendEmailVerifyCode(String sessionId, SSOClientUser userClient) {
		// 邮箱验证
		String email = userClient.getEmail();
		String nickName = userClient.getNickName();
		SendEmailRequest emailRequest = new SendEmailRequest();
		emailRequest.setTomails(new String[] { email });
		emailRequest.setTemplateRUL(UpdateEmail.TEMPLATE_EMAIL_URL);
		// 验证码
		String verifyCode = RandomUtil.randomNum(EmailVerifyConstants.VERIFY_SIZE);
		// 将验证码放入缓存
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(UpdateEmail.CACHE_NAMESPACE);
		String cacheKey = UpdateEmail.CACHE_KEY_VERIFY_EMAIL + sessionId;
		cacheClient.setex(cacheKey, EmailVerifyConstants.VERIFY_OVERTIME, verifyCode);
		// 超时时间
		String overTime = ObjectUtils.toString(EmailVerifyConstants.VERIFY_OVERTIME / 60);
		emailRequest.setData(new String[] { nickName, verifyCode, overTime });
		return VerifyUtil.sendEmail(emailRequest);
	}
	
	/**
	 * 身份认证
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/confirmInfo")
	@ResponseBody
	public ResponseData<String> confirmInfo(HttpServletRequest request, SafetyConfirmData safetyConfirmData) {
		String confirmType = safetyConfirmData.getConfirmType();
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(UpdateEmail.CACHE_NAMESPACE);
		String sessionId = request.getSession().getId();
		// 检查图片验证码
		ResponseData<String> pictureCheck = checkPictureVerifyCode(safetyConfirmData, cacheClient, sessionId);
		if (ResponseData.AJAX_STATUS_FAILURE.equals(pictureCheck.getStatusCode())) {
			return pictureCheck;
		}
		// 检查短信或邮箱验证码
		if (UpdateEmail.CHECK_TYPE_PHONE.equals(confirmType)) {
			// 检查短信验证码
			ResponseData<String> phoneCheck = checkPhoneVerifyCode(safetyConfirmData, cacheClient, sessionId);
			if (ResponseData.AJAX_STATUS_FAILURE.equals(phoneCheck.getStatusCode())) {
				return phoneCheck;
			}

		} else if (UpdateEmail.CHECK_TYPE_EMAIL.equals(confirmType)) {
			// 检查邮箱验证码
			ResponseData<String> emailCheck = checkEmailVerifyCode(safetyConfirmData, cacheClient, sessionId);
			if (ResponseData.AJAX_STATUS_FAILURE.equals(emailCheck.getStatusCode())) {
				return emailCheck;
			}
		}
		//用户信息放入缓存
		String uuid = UUIDUtil.genId32();
		SSOClientUser userClient = (SSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
		CacheUtil.setValue(uuid, Constants.UUID.OVERTIME, userClient, Constants.UpdateEmail.CACHE_NAMESPACE);
		return new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "正确", "/center/email/setEmail?"+Constants.UUID.KEY_NAME+"="+uuid);
	}

	/**
	 * 检查图片验证码
	 * 
	 * @param safetyConfirmData
	 * @param cacheClient
	 * @param sessionId
	 * @return
	 */
	private ResponseData<String> checkPictureVerifyCode(SafetyConfirmData safetyConfirmData, ICacheClient cacheClient, String sessionId) {
		String pictureVerifyCodeCache = cacheClient.get(UpdateEmail.CACHE_KEY_VERIFY_PICTURE + sessionId);
		String pictureVerifyCode = safetyConfirmData.getPictureVerifyCode();
		ResponseData<String> responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "正确", null);
		if (pictureVerifyCodeCache != null) {
			if (pictureVerifyCodeCache.compareToIgnoreCase(pictureVerifyCode) != 0) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "图形验证码错误", null);
			}
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "图形验证码已失效", null);
		}
		return responseData;
	}

	/**
	 * 检查邮箱验证码
	 * 
	 * @param safetyConfirmData
	 * @param cacheClient
	 * @param sessionId
	 * @return
	 */
	private ResponseData<String> checkPhoneVerifyCode(SafetyConfirmData safetyConfirmData, ICacheClient cacheClient, String sessionId) {
		String cacheKey = UpdateEmail.CACHE_KEY_VERIFY_PHONE + sessionId;
		String verifyCodeCache = cacheClient.get(cacheKey);
		String verifyCode = safetyConfirmData.getVerifyCode();
		ResponseData<String> responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "正确", null);
		if (verifyCodeCache != null) {
			if (!verifyCodeCache.equals(verifyCode)) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "手机校验码错误", null);
			}
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "手机校验码已失效", null);
		}
		return responseData;
	}

	/**
	 * 检查邮箱验证码
	 * 
	 * @param safetyConfirmData
	 * @param cacheClient
	 * @param sessionId
	 * @return
	 */
	private ResponseData<String> checkEmailVerifyCode(SafetyConfirmData safetyConfirmData, ICacheClient cacheClient, String sessionId) {
		String cacheKey = UpdateEmail.CACHE_KEY_VERIFY_EMAIL + sessionId;
		String verifyCodeCache = cacheClient.get(cacheKey);
		String verifyCode = safetyConfirmData.getVerifyCode();
		ResponseData<String> responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "正确", null);
		if (verifyCodeCache != null) {
			if (!verifyCodeCache.equals(verifyCode)) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "邮箱校验码错误", null);
			}
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "邮箱校验码已失效", null);
		}
		return responseData;
	}

	/**
	 * 修改邮箱页跳转
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/setEmail")
	public ModelAndView updateEmailPage(HttpServletRequest request) {
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser)CacheUtil.getValue(uuid, Constants.UpdateEmail.CACHE_NAMESPACE, SSOClientUser.class);
		if(userClient == null){
			return updateEmailStart(request);
		}
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("uuid", uuid);
		return new ModelAndView("jsp/center/update-email-new",model);
	}
	
	/**
	 * 发送邮件验证码(修改新邮箱时验证)
	 * @param request
	 * @param sessionId
	 * @param email
	 * @return
	 */
	@RequestMapping("/sendEmailVerify")
	@ResponseBody
	public ResponseData<String> sendEmailVerifyCode(HttpServletRequest request, String email) {
		//SSOClientUser userClient = (SSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser)CacheUtil.getValue(uuid, Constants.UpdateEmail.CACHE_NAMESPACE, SSOClientUser.class);
		if(userClient == null){
			return new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS,"身份认证失效","/center/email/confirminfo");
		}
		String nickName = userClient.getNickName();
		SendEmailRequest emailRequest = new SendEmailRequest();
		emailRequest.setTomails(new String[] { email });
		emailRequest.setTemplateRUL(UpdateEmail.TEMPLATE_SETEMAIL_URL);
		// 验证码
		String verifyCode = RandomUtil.randomNum(EmailVerifyConstants.VERIFY_SIZE);
		// 将验证码放入缓存
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(UpdateEmail.CACHE_NAMESPACE);
		String cacheKey = UpdateEmail.CACHE_KEY_VERIFY_SETEMAIL + request.getSession().getId();
		cacheClient.setex(cacheKey, EmailVerifyConstants.VERIFY_OVERTIME, verifyCode);
		// 超时时间
		String overTime = ObjectUtils.toString(EmailVerifyConstants.VERIFY_OVERTIME / 60);
		emailRequest.setData(new String[] { nickName, verifyCode, overTime });
		boolean isSuccess =  VerifyUtil.sendEmail(emailRequest);
		if(isSuccess){
			return new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS,"发送成功",null);
		}else{
			return new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,"发送失败,请稍后再试",null);
		}
	}

	
	/**
	 * 设置新邮箱
	 * 
	 * @param request
	 * @param newPassword
	 * @return
	 */
	@RequestMapping("/setNewEmail")
	@ResponseBody
	public ResponseData<String> setNewEmail(HttpServletRequest request, String email, String verifyCode) {
		//SSOClientUser userClient = (SSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser)CacheUtil.getValue(uuid, Constants.UpdateEmail.CACHE_NAMESPACE, SSOClientUser.class);
		if(userClient == null){
			return new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS,"身份认证失效","/center/email/confirminfo");
		}
		//检查验证码
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(UpdateEmail.CACHE_NAMESPACE);
 		ResponseData<String> responseData = checkSetEmailVerifyCode(verifyCode, cacheClient, request.getSession().getId());
		if(ResponseData.AJAX_STATUS_FAILURE.equals(responseData.getStatusCode())){
			return responseData;
		}
		//更新邮箱
		IAccountSecurityManageSV accountSecurityManageSV = DubboConsumerFactory.getService("iAccountSecurityManageSV");
		AccountEmailRequest accountEmailRequest = new AccountEmailRequest();
		accountEmailRequest.setAccountId(userClient.getAccountId());
		accountEmailRequest.setEmail(email);
		accountEmailRequest.setUpdateAccountId(userClient.getAccountId());
		BaseResponse resultData = accountSecurityManageSV.setEmailData(accountEmailRequest );
		ResponseHeader responseHeader = resultData.getResponseHeader();
		String resultCode = responseHeader.getResultCode();
		String resultMessage = responseHeader.getResultMessage();
		if (ResultCode.SUCCESS_CODE.equals(resultCode)) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "修改邮箱成功", "/center/email/success");
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, resultMessage, null);
		}
		CacheUtil.deletCache(uuid, Constants.UpdateEmail.CACHE_NAMESPACE);
		return responseData;
	}
	
	/**
	 * 检查邮箱验证码
	 * 
	 * @param safetyConfirmData
	 * @param cacheClient
	 * @param sessionId
	 * @return
	 */
	private ResponseData<String> checkSetEmailVerifyCode( String verifyCode,ICacheClient cacheClient, String sessionId) {
		String cacheKey = UpdateEmail.CACHE_KEY_VERIFY_SETEMAIL + sessionId;
		String verifyCodeCache = cacheClient.get(cacheKey);
		ResponseData<String> responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "正确", "正确");
		if (verifyCodeCache != null) {
			if (!verifyCodeCache.equals(verifyCode)) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "邮箱校验码错误", "邮箱校验码错误");
			}
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "邮箱校验码已失效", "邮箱校验码已失效");
		}
		return responseData;
	}

	@RequestMapping("/success")
	public ModelAndView successPage() {
		return new ModelAndView("jsp/center/update-email-success");
	}
}
