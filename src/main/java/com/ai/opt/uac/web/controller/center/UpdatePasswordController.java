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
import com.ai.opt.sdk.util.Md5Encoder;
import com.ai.opt.sdk.util.RandomUtil;
import com.ai.opt.sdk.util.StringUtil;
import com.ai.opt.sdk.util.UUIDUtil;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.sso.client.filter.SSOClientConstants;
import com.ai.opt.sso.client.filter.SSOClientUser;
import com.ai.opt.uac.api.security.interfaces.IAccountSecurityManageSV;
import com.ai.opt.uac.api.security.param.AccountPasswordRequest;
import com.ai.opt.uac.web.constants.Constants;
import com.ai.opt.uac.web.constants.Constants.ResultCode;
import com.ai.opt.uac.web.constants.Constants.RetakePassword;
import com.ai.opt.uac.web.constants.Constants.SMSUtil;
import com.ai.opt.uac.web.constants.Constants.UpdatePassword;
import com.ai.opt.uac.web.constants.VerifyConstants;
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

@RequestMapping("/center/password")
@Controller
public class UpdatePasswordController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePasswordController.class);

	@RequestMapping("/confirminfo")
	public ModelAndView UpdatePasswordStart(HttpServletRequest request) {
		SSOClientUser userClient = (SSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
		if (userClient != null) {
			Map<String, AccountData> model = new HashMap<String, AccountData>();
			String phone = userClient.getPhone();
			String email = userClient.getEmail();
			AccountData confirmInfo = new AccountData(phone, email);
			model.put("confirmInfo", confirmInfo);
			return new ModelAndView("jsp/center/update-password-start", model);
		} else {
			return new ModelAndView("jsp/center/update-password-start");
		}
	}

	@RequestMapping("/getImageVerifyCode")
	@ResponseBody
	public void getImageVerifyCode(HttpServletRequest request, HttpServletResponse response) {
		String cacheKey = UpdatePassword.CACHE_KEY_VERIFY_PICTURE + request.getSession().getId();
		BufferedImage image = VerifyUtil.getImageVerifyCode(request, UpdatePassword.CACHE_NAMESPACE, cacheKey);
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
		ResponseHeader responseHeader = null;
		String sessionId = request.getSession().getId();
		if (userClient != null) {
			if (UpdatePassword.CHECK_TYPE_PHONE.equals(checkType)) {
				// 发送手机验证码
				String isSuccess = sendPhoneVerifyCode(sessionId, userClient);
				if (isSuccess.equals("0000")) {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "短信验证码发送成功", "短信验证码发送成功");
					ResponseHeader header = new ResponseHeader();
					header.setIsSuccess(true);
					header.setResultCode(ResultCode.SUCCESS_CODE);
					responseData.setResponseHeader(header);
					return responseData;
				} else if (isSuccess.equals("0002")) {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "短信验证码发送失败", "重复发送");
					ResponseHeader header = new ResponseHeader();
					header.setIsSuccess(false);
					header.setResultCode(SMSUtil.CACHE_SMS_ERROR_CODE);
					header.setResultMessage("重复发送");
					responseData.setResponseHeader(header);
					return responseData;
				} else {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "短信验证码发送失败", "服务器连接超时");
					ResponseHeader header = new ResponseHeader();
					header.setIsSuccess(false);
					header.setResultCode(ResultCode.ERROR_CODE);
					responseData.setResponseHeader(header);
					return responseData;
				}
			} else if (UpdatePassword.CHECK_TYPE_EMAIL.equals(checkType)) {
				// 发送邮件验证码
				String isSuccess = sendEmailVerifyCode(sessionId, userClient);
				if (isSuccess.equals("0000")) {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "短信验证码发送成功", "短信验证码发送成功");
					ResponseHeader header = new ResponseHeader();
					header.setIsSuccess(true);
					header.setResultCode(ResultCode.SUCCESS_CODE);
					responseData.setResponseHeader(header);
					return responseData;
				} else if (isSuccess.equals("0002")) {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "短信验证码发送失败", "重复发送");
					ResponseHeader header = new ResponseHeader();
					header.setIsSuccess(false);
					header.setResultCode(SMSUtil.CACHE_SMS_ERROR_CODE);
					header.setResultMessage("重复发送");
					responseData.setResponseHeader(header);
					return responseData;
				} else {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "短信验证码发送失败", "服务器连接超时");
					ResponseHeader header = new ResponseHeader();
					header.setIsSuccess(false);
					header.setResultCode(ResultCode.ERROR_CODE);
					responseData.setResponseHeader(header);
					return responseData;
				}

			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "验证码发送失败,验证方式不正确", null);
				responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.ERROR_CODE, "验证码发送失败");
			}
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "验证码发送失败", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.ERROR_CODE, "验证码发送失败");
		}
		responseData.setResponseHeader(responseHeader);
		return responseData;
	}

	/**
	 * 发送手机验证码
	 * 
	 * @param userClient
	 */
	private String sendPhoneVerifyCode(String sessionId, SSOClientUser userClient) {
		SMDataInfoNotify smDataInfoNotify = new SMDataInfoNotify();
		String phoneVerifyCode = RandomUtil.randomNum(PhoneVerifyConstants.VERIFY_SIZE);
		// 查询是否发送过短信
		String smstimes = "1";
		String smskey = SMSUtil.CACHE_KEY_SMS_UPDATE_PASSWORD + userClient.getPhone();
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(UpdatePassword.CACHE_NAMESPACE);
		String times = cacheClient.get(smskey);
		if (StringUtil.isBlank(times)) {
			// 将验证码放入缓存
			String cacheKey = UpdatePassword.CACHE_KEY_VERIFY_PHONE + sessionId;
			cacheClient.setex(cacheKey, PhoneVerifyConstants.VERIFY_OVERTIME, phoneVerifyCode);
			// 将发送次数放入缓存
			cacheClient.setex(smskey, SMSUtil.SMS_VERIFY_TIMES, smstimes);
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
			boolean flag = VerifyUtil.sendPhoneInfo(smDataInfoNotify);
			if (flag) {
				// 成功
				return "0000";
			} else {
				// 失败
				return "0001";
			}
		} else {
			// 重复发送
			return "0002";
		}

	}

	/**
	 * 发送邮件验证码
	 * 
	 * @param accountInfo
	 */
	private String sendEmailVerifyCode(String sessionId, SSOClientUser userClient) {
		// 查询是否发送过邮件
		String smstimes = "1";
		String smskey = SMSUtil.CACHE_KEY_SMS_UPDATE_PASSWORD + userClient.getPhone();
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(UpdatePassword.CACHE_NAMESPACE);
		String times = cacheClient.get(smskey);
		if (StringUtil.isBlank(times)) {
			// 邮箱验证
			String email = userClient.getEmail();
			String nickName = userClient.getNickName();
			SendEmailRequest emailRequest = new SendEmailRequest();
			emailRequest.setTomails(new String[] { email });
			emailRequest.setTemplateRUL(UpdatePassword.TEMPLATE_EMAIL_URL);
			// 验证码
			String verifyCode = RandomUtil.randomNum(EmailVerifyConstants.VERIFY_SIZE);
			// 将验证码放入缓存
			String cacheKey = UpdatePassword.CACHE_KEY_VERIFY_EMAIL + sessionId;
			cacheClient.setex(cacheKey, EmailVerifyConstants.VERIFY_OVERTIME, verifyCode);
			// 将发送次数放入缓存
			cacheClient.setex(smskey, SMSUtil.SMS_VERIFY_TIMES, smstimes);
			// 超时时间
			String overTime = ObjectUtils.toString(EmailVerifyConstants.VERIFY_OVERTIME / 60);
			emailRequest.setData(new String[] { nickName, verifyCode, overTime });
			boolean flag = VerifyUtil.sendEmail(emailRequest);
			if (flag) {
				// 成功
				return "0000";
			} else {
				// 失败
				return "0001";
			}
		} else {
			// 重复发送
			return "0002";
		}
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
		ResponseData<String> responseData = null;
		String confirmType = safetyConfirmData.getConfirmType();
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(UpdatePassword.CACHE_NAMESPACE);
		String sessionId = request.getSession().getId();
		// 检查图片验证码
		String pictureVerifyCodeCache = cacheClient.get(UpdatePassword.CACHE_KEY_VERIFY_PICTURE + sessionId);
		String pictureVerifyCode = safetyConfirmData.getPictureVerifyCode();
		ResponseData<String> pictureCheck = VerifyUtil.checkPictureVerifyCode(pictureVerifyCode, pictureVerifyCodeCache);
		String resultCode = pictureCheck.getResponseHeader().getResultCode();
		if (!VerifyConstants.ResultCodeConstants.SUCCESS_CODE.equals(resultCode)) {
			return pictureCheck;
		}
		// 检查短信或邮箱验证码
		if (RetakePassword.CHECK_TYPE_PHONE.equals(confirmType)) {
			// 检查短信验证码
			String verifyCodeCache = cacheClient.get(UpdatePassword.CACHE_KEY_VERIFY_PHONE + sessionId);
			String verifyCode = safetyConfirmData.getVerifyCode();
			ResponseData<String> phoneCheck = VerifyUtil.checkPhoneVerifyCode(verifyCode, verifyCodeCache);
			String phoneResultCode = phoneCheck.getResponseHeader().getResultCode();
			if (!VerifyConstants.ResultCodeConstants.SUCCESS_CODE.equals(phoneResultCode)) {
				return phoneCheck;
			}
		} else if (RetakePassword.CHECK_TYPE_EMAIL.equals(confirmType)) {
			// 检查邮箱验证码
			String verifyCodeCache = cacheClient.get(UpdatePassword.CACHE_KEY_VERIFY_EMAIL + sessionId);
			String verifyCode = safetyConfirmData.getVerifyCode();
			ResponseData<String> emailCheck = VerifyUtil.checkEmailVerifyCode(verifyCode, verifyCodeCache);
			String emailResultCode = emailCheck.getResponseHeader().getResultCode();
			if (!VerifyConstants.ResultCodeConstants.SUCCESS_CODE.equals(emailResultCode)) {
				return emailCheck;
			}
		}
		// 用户信息放入缓存
		String uuid = UUIDUtil.genId32();
		SSOClientUser userClient = (SSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
		CacheUtil.setValue(uuid, Constants.UUID.OVERTIME, userClient, Constants.UpdatePassword.CACHE_NAMESPACE);
		responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "正确", "/center/password/setPassword?" + Constants.UUID.KEY_NAME + "=" + uuid);
		ResponseHeader responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "正确");
		responseData.setResponseHeader(responseHeader);
		return responseData;
	}

	/**
	 * 修改邮箱页跳转
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/setPassword")
	public ModelAndView UpdatePasswordPage(HttpServletRequest request) {
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.UpdatePassword.CACHE_NAMESPACE, SSOClientUser.class);
		if (userClient == null) {
			return new ModelAndView("redirect:/center/password/confirminfo");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("uuid", uuid);
		return new ModelAndView("jsp/center/update-password-new", model);
	}

	/**
	 * 设置新邮箱
	 * 
	 * @param request
	 * @param newPassword
	 * @return
	 */
	@RequestMapping("/setNewPassword")
	@ResponseBody
	public ResponseData<String> setNewPassword(HttpServletRequest request, String password) {
		ResponseData<String> responseData = null;
		ResponseHeader responseHeader = null;
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.UpdatePassword.CACHE_NAMESPACE, SSOClientUser.class);
		if (userClient == null) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "身份认证失效", "/center/password/confirminfo");
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "认证身份失效");
		} else {
			// 更新邮箱
			IAccountSecurityManageSV accountSecurityManageSV = DubboConsumerFactory.getService("iAccountSecurityManageSV");
			AccountPasswordRequest accountPasswordRequest = new AccountPasswordRequest();
			accountPasswordRequest.setAccountId(userClient.getAccountId());
			accountPasswordRequest.setAccountPassword(Md5Encoder.encodePassword(password));
			accountPasswordRequest.setUpdateAccountId(userClient.getAccountId());
			BaseResponse resultData = accountSecurityManageSV.setPasswordData(accountPasswordRequest);
			if (ResultCode.SUCCESS_CODE.equals(resultData.getResponseHeader().getResultCode())) {
				String newuuid = UUIDUtil.genId32();
				CacheUtil.setValue(newuuid, Constants.UUID.OVERTIME, userClient, Constants.UpdateEmail.CACHE_NAMESPACE);
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "修改密码成功", "/center/password/success?" + Constants.UUID.KEY_NAME + "=" + newuuid);
				responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "修改密码成功");
				CacheUtil.deletCache(uuid, Constants.UpdatePassword.CACHE_NAMESPACE);
			} else {
				String resultMessage = resultData.getResponseHeader().getResultMessage();
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, resultMessage, null);
				responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.ERROR_CODE, "修改密码失败");
			}
		}
		responseData.setResponseHeader(responseHeader);
		return responseData;
	}

	@RequestMapping("/success")
	public ModelAndView successPage(HttpServletRequest request) {
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.UpdatePassword.CACHE_NAMESPACE, SSOClientUser.class);
		if (userClient == null) {
			return new ModelAndView("redirect:/center/password/confirminfo");
		}
		CacheUtil.deletCache(uuid, Constants.UpdatePassword.CACHE_NAMESPACE);
		return new ModelAndView("jsp/center/update-password-success");
	}
}
