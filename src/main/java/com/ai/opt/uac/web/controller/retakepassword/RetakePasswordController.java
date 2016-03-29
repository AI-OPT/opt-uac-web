package com.ai.opt.uac.web.controller.retakepassword;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
import com.ai.opt.sdk.util.BeanUtils;
import com.ai.opt.sdk.util.DubboConsumerFactory;
import com.ai.opt.sdk.util.Md5Encoder;
import com.ai.opt.sdk.util.RandomUtil;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.sso.client.filter.SSOClientUser;
import com.ai.opt.uac.api.account.interfaces.IAccountManageSV;
import com.ai.opt.uac.api.account.param.AccountQueryRequest;
import com.ai.opt.uac.api.account.param.AccountQueryResponse;
import com.ai.opt.uac.api.security.interfaces.IAccountSecurityManageSV;
import com.ai.opt.uac.api.security.param.AccountPasswordRequest;
import com.ai.opt.uac.api.sso.interfaces.ILoginSV;
import com.ai.opt.uac.api.sso.param.UserLoginResponse;
import com.ai.opt.uac.web.constants.Constants;
import com.ai.opt.uac.web.constants.Constants.ResultCode;
import com.ai.opt.uac.web.constants.Constants.RetakePassword;
import com.ai.opt.uac.web.constants.VerifyConstants.EmailVerifyConstants;
import com.ai.opt.uac.web.constants.VerifyConstants.PhoneVerifyConstants;
import com.ai.opt.uac.web.model.email.SendEmailRequest;
import com.ai.opt.uac.web.model.retakepassword.AccountData;
import com.ai.opt.uac.web.model.retakepassword.SafetyConfirmData;
import com.ai.opt.uac.web.model.retakepassword.SendVerifyRequest;
import com.ai.opt.uac.web.util.VerifyUtil;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;
import com.ai.runner.center.mmp.api.manager.param.SMData;
import com.ai.runner.center.mmp.api.manager.param.SMDataInfoNotify;

@RequestMapping("/retakePassword")
@Controller
public class RetakePasswordController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RetakePasswordController.class);
	
	/**
	 * 填写用户名
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/userinfo")
	public ModelAndView userInfo(HttpServletRequest request) {
		return new ModelAndView("jsp/retakepassword/userinfo");
	}
	
	/**
	 * 获得账户信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkUserInfo")
	@ResponseBody
	public ResponseData<String> checkUserInfo(HttpServletRequest request, String username) {
		
		LOGGER.info("查询账户信息开始，查询参数为： username=" + username);
		// 获取账户信息
		ILoginSV loginService = DubboConsumerFactory.getService("iLoginSV");
		UserLoginResponse userLoginResponse = loginService.queryAccountByUserName(username);
		if(userLoginResponse != null){
			ResponseHeader responseHeader = userLoginResponse.getResponseHeader();
			if(Constants.ResultCode.SUCCESS_CODE.equals(responseHeader.getResultCode())){
				SSOClientUser ssoClientUser = new SSOClientUser();
				BeanUtils.copyProperties(ssoClientUser, userLoginResponse);
				request.getSession().setAttribute(Constants.RetakePassword.USER_SESSION_KEY, ssoClientUser);
				return new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "该用户存在", "/retakePassword/confirminfo");
			}
		}
		return new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "该用户不存在", null);
	}

	/**
	 * 身份认证界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/confirminfo")
	public ModelAndView confirmInfo(HttpServletRequest request) {
		return new ModelAndView("jsp/retakepassword/confirminfo");
	}

	/**
	 * 获得账户信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getAccountInfo")
	@ResponseBody
	public ResponseData<AccountData> getAccountInfo(HttpServletRequest request) {
		// UserLoginResponse userLoginResponse = (UserLoginResponse)
		// request.getSession().getAttribute(RetakePassword.USER_SESSION_KEY);
		
		Long accountId = 1L;
		LOGGER.info("查询账户信息开始，查询参数为： accountId=" + accountId);
		AccountQueryRequest accountQueryRequest = new AccountQueryRequest();
		accountQueryRequest.setAccountId(accountId);
		// 获取账户信息
		AccountQueryResponse accountQueryResponse = getAccountInfoById(accountId);

		// 加密
		String phone = accountQueryResponse.getPhone();
		String email = accountQueryResponse.getEmail();
		AccountData confirmInfo = new AccountData(phone, email);
		return new ResponseData<AccountData>(ResponseData.AJAX_STATUS_SUCCESS, "信息查询成功", confirmInfo);
	}

	@RequestMapping("/getImageVerifyCode")
	@ResponseBody
	public void getImageVerifyCode(HttpServletRequest request, HttpServletResponse response) {
		String cacheKey = RetakePassword.CACHE_KEY_VERIFY_PICTURE + request.getSession().getId();
		BufferedImage image = VerifyUtil.getImageVerifyCode(request, RetakePassword.CACHE_NAMESPACE, cacheKey);
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
		// UserLoginResponse userLoginResponse = (UserLoginResponse)
		// request.getSession().getAttribute(RetakePassword.USER_SESSION_KEY);
		String checkType = sendVerifyRequest.getCheckType();
		ResponseData<String> responseData = null;
		String sessionId = request.getSession().getId();

		UserLoginResponse userLoginResponse = new UserLoginResponse();
		AccountQueryResponse accountQueryResponse = getAccountInfoById(1L);
		BeanUtils.copyProperties(userLoginResponse, accountQueryResponse);

		if (userLoginResponse != null) {
			if (RetakePassword.CHECK_TYPE_PHONE.equals(checkType)) {
				// 发送手机验证码
				boolean isSuccess = sendPhoneVerifyCode(sessionId, userLoginResponse);
				if (isSuccess) {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "短信验证码发送成功", "短信验证码发送成功");
				} else {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "短信验证码发送失败", "服务器连接超时");
				}
			} else if (RetakePassword.CHECK_TYPE_EMAIL.equals(checkType)) {
				// 发送邮件验证码
				boolean isSuccess = sendEmailVerifyCode(sessionId, userLoginResponse);
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
	 * @param userLoginResponse
	 */
	private boolean sendPhoneVerifyCode(String sessionId, UserLoginResponse userLoginResponse) {
		SMDataInfoNotify smDataInfoNotify = new SMDataInfoNotify();
		String phoneVerifyCode = RandomUtil.randomNum(PhoneVerifyConstants.VERIFY_SIZE);
		// 将验证码放入缓存
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(RetakePassword.CACHE_NAMESPACE);
		String cacheKey = RetakePassword.CACHE_KEY_VERIFY_PHONE + sessionId;
		cacheClient.setex(cacheKey, PhoneVerifyConstants.VERIFY_OVERTIME, phoneVerifyCode);
		// 设置短息信息
		List<SMData> dataList = new LinkedList<SMData>();
		SMData smData = new SMData();
		smData.setGsmContent("${VERIFY}:" + phoneVerifyCode + "^${VALIDMINS}:" + PhoneVerifyConstants.VERIFY_OVERTIME / 60);
		smData.setPhone(userLoginResponse.getPhone());
		smData.setTemplateId(PhoneVerifyConstants.TEMPLATE_RETAKE_PASSWORD_ID);
		smData.setServiceType(PhoneVerifyConstants.SERVICE_TYPE);
		dataList.add(smData);
		smDataInfoNotify.setDataList(dataList);
		smDataInfoNotify.setMsgSeq(VerifyUtil.createPhoneMsgSeq());
		smDataInfoNotify.setTenantId(userLoginResponse.getTenantId());
		smDataInfoNotify.setSystemId(Constants.SYSTEM_ID);
		return VerifyUtil.sendPhoneInfo(smDataInfoNotify);
	}

	/**
	 * 发送邮件验证码
	 * 
	 * @param accountInfo
	 */
	private boolean sendEmailVerifyCode(String sessionId, UserLoginResponse userLoginResponse) {
		// 邮箱验证
		String email = userLoginResponse.getEmail();
		String nickName = userLoginResponse.getNickName();
		SendEmailRequest emailRequest = new SendEmailRequest();
		emailRequest.setTomails(new String[] { email });
		emailRequest.setTemplateRUL(RetakePassword.TEMPLATE_EMAIL_URL);
		// 验证码
		String verifyCode = RandomUtil.randomNum(EmailVerifyConstants.VERIFY_SIZE);
		// 将验证码放入缓存
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(RetakePassword.CACHE_NAMESPACE);
		String cacheKey = RetakePassword.CACHE_KEY_VERIFY_EMAIL + sessionId;
		cacheClient.setex(cacheKey, EmailVerifyConstants.VERIFY_OVERTIME, verifyCode);
		// 超时时间
		String overTime = ObjectUtils.toString(EmailVerifyConstants.VERIFY_OVERTIME / 60);
		emailRequest.setData(new String[] { nickName, verifyCode, overTime });
		return VerifyUtil.sendEmail(emailRequest);
	}

	/**
	 * 获得账户信息
	 * 
	 * @param accountId
	 */
	private AccountQueryResponse getAccountInfoById(Long accountId) {
		AccountQueryRequest accountQueryRequest = new AccountQueryRequest();
		accountQueryRequest.setAccountId(accountId);
		IAccountManageSV accountManageSV = DubboConsumerFactory.getService("iAccountManageSV");
		return accountManageSV.queryBaseInfo(accountQueryRequest);
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
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(RetakePassword.CACHE_NAMESPACE);
		String sessionId = request.getSession().getId();
		// 检查图片验证码
		ResponseData<String> pictureCheck = checkPictureVerifyCode(safetyConfirmData, cacheClient, sessionId);
		if (ResponseData.AJAX_STATUS_FAILURE.equals(pictureCheck.getStatusCode())) {
			return pictureCheck;
		}
		// 检查短信或邮箱验证码
		if (RetakePassword.CHECK_TYPE_PHONE.equals(confirmType)) {
			// 检查短信验证码
			ResponseData<String> phoneCheck = checkPhoneVerifyCode(safetyConfirmData, cacheClient, sessionId);
			if (ResponseData.AJAX_STATUS_FAILURE.equals(phoneCheck.getStatusCode())) {
				return phoneCheck;
			}

		} else if (RetakePassword.CHECK_TYPE_EMAIL.equals(confirmType)) {
			// 检查邮箱验证码
			ResponseData<String> emailCheck = checkEmailVerifyCode(safetyConfirmData, cacheClient, sessionId);
			if (ResponseData.AJAX_STATUS_FAILURE.equals(emailCheck.getStatusCode())) {
				return emailCheck;
			}
		}
		return new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "正确", "/retakePassword/resetPassword");
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
		String pictureVerifyCodeCache = cacheClient.get(RetakePassword.CACHE_KEY_VERIFY_PICTURE + sessionId);
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
		String cacheKey = RetakePassword.CACHE_KEY_VERIFY_PHONE + sessionId;
		String verifyCodeCache = cacheClient.get(cacheKey);
		String verifyCode = safetyConfirmData.getVerifyCode();
		ResponseData<String> responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "正确", null);
		;
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
		String cacheKey = RetakePassword.CACHE_KEY_VERIFY_EMAIL + sessionId;
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
	 * 重置密码页跳转
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/resetPassword")
	public ModelAndView resetPassword(HttpServletRequest request) {
		return new ModelAndView("jsp/retakepassword/resetpassword");
	}

	/**
	 * 设置密码
	 * 
	 * @param request
	 * @param newPassword
	 * @return
	 */
	@RequestMapping("/setNewPassword")
	@ResponseBody
	public ResponseData<String> setNewPassword(HttpServletRequest request, String password) {
		ResponseData<String> responseData = null;
		IAccountSecurityManageSV accountManageSV = DubboConsumerFactory.getService("iAccountSecurityManageSV");
		AccountPasswordRequest passwordRequest = new AccountPasswordRequest();
		passwordRequest.setAccountId(1L);
		String encodePassword = Md5Encoder.encodePassword(password);
		passwordRequest.setAccountPassword(encodePassword);
		passwordRequest.setUpdateAccountId(1L);
		BaseResponse resultData = accountManageSV.setPasswordData(passwordRequest);
		ResponseHeader responseHeader = resultData.getResponseHeader();
		String resultCode = responseHeader.getResultCode();
		String resultMessage = responseHeader.getResultMessage();
		if (ResultCode.SUCCESS_CODE.equals(resultCode)) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "重置密码成功", "/retakePassword/success");
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, resultMessage, resultMessage);
		}
		return responseData;
	}

	@RequestMapping("/success")
	public ModelAndView successPage() {
		return new ModelAndView("jsp/retakepassword/retaksuccess");
	}

}
