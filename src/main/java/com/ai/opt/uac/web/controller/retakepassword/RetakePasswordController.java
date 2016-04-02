package com.ai.opt.uac.web.controller.retakepassword;

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
import com.ai.opt.sdk.configcenter.factory.ConfigCenterFactory;
import com.ai.opt.sdk.util.BeanUtils;
import com.ai.opt.sdk.util.DubboConsumerFactory;
import com.ai.opt.sdk.util.Md5Encoder;
import com.ai.opt.sdk.util.RandomUtil;
import com.ai.opt.sdk.util.StringUtil;
import com.ai.opt.sdk.util.UUIDUtil;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.sso.client.filter.SSOClientUser;
import com.ai.opt.sso.client.filter.SSOClientUtil;
import com.ai.opt.uac.api.security.interfaces.IAccountSecurityManageSV;
import com.ai.opt.uac.api.security.param.AccountPasswordRequest;
import com.ai.opt.uac.api.sso.interfaces.ILoginSV;
import com.ai.opt.uac.api.sso.param.UserLoginResponse;
import com.ai.opt.uac.web.constants.Constants;
import com.ai.opt.uac.web.constants.Constants.ResultCode;
import com.ai.opt.uac.web.constants.Constants.RetakePassword;
import com.ai.opt.uac.web.constants.Constants.SMSUtil;
import com.ai.opt.uac.web.constants.VerifyConstants;
import com.ai.opt.uac.web.constants.VerifyConstants.EmailVerifyConstants;
import com.ai.opt.uac.web.constants.VerifyConstants.PhoneVerifyConstants;
import com.ai.opt.uac.web.model.email.SendEmailRequest;
import com.ai.opt.uac.web.model.login.LoginUser;
import com.ai.opt.uac.web.model.retakepassword.AccountData;
import com.ai.opt.uac.web.model.retakepassword.SafetyConfirmData;
import com.ai.opt.uac.web.model.retakepassword.SendVerifyRequest;
import com.ai.opt.uac.web.util.CacheUtil;
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

	@RequestMapping("/getUserImageVerifyCode")
	@ResponseBody
	public void getUserImageVerifyCode(HttpServletRequest request, HttpServletResponse response) {
		String cacheKey = RetakePassword.CACHE_KEY_VERIFY_PICTURE_USER + request.getSession().getId();
		BufferedImage image = VerifyUtil.getImageVerifyCode(request, RetakePassword.CACHE_NAMESPACE, cacheKey);
		try {
			ImageIO.write(image, "PNG", response.getOutputStream());
		} catch (IOException e) {
			LOGGER.error("生成图片验证码错误：" + e);
			e.printStackTrace();
		}
	}

	/**
	 * 获得账户信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkUserInfo")
	@ResponseBody
	public ResponseData<String> checkUserInfo(HttpServletRequest request, String username, String pictureVerifyCode) {
		LOGGER.info("查询账户信息开始，查询参数为： username=" + username);
		ResponseData<String> responseData = null;
		String cacheKey = Constants.RetakePassword.CACHE_KEY_VERIFY_PICTURE_USER + request.getSession().getId();
		// 检查图片验证码
		ResponseData<String> pictureCheck = checkPictureVerifyCode(pictureVerifyCode, cacheKey);
		String resultCode = pictureCheck.getResponseHeader().getResultCode();
		if (!VerifyConstants.ResultCodeConstants.SUCCESS_CODE.equals(resultCode)) {
			responseData = pictureCheck;
		} else {
			// 检查用户名是否存在
			responseData = checkUserNameExist(username);
		}
		return responseData;
	}

	/**
	 * 检查用户名是否存在
	 * 
	 * @param username
	 * @return
	 */
	private ResponseData<String> checkUserNameExist(String username) {
		ResponseData<String> responseData = null;
		ResponseHeader responseHeader = null;
		// 获取账户信息
		ILoginSV loginService = DubboConsumerFactory.getService("iLoginSV");
		UserLoginResponse userLoginResponse = loginService.queryAccountByUserName(username);
		if (userLoginResponse != null && Constants.ResultCode.SUCCESS_CODE.equals(userLoginResponse.getResponseHeader().getResultCode())) {
			SSOClientUser ssoClientUser = new SSOClientUser();
			BeanUtils.copyProperties(ssoClientUser, userLoginResponse);
			String uuid = UUIDUtil.genId32();
			// 放入缓存
			CacheUtil.setValue(uuid, Constants.UUID.OVERTIME, ssoClientUser, Constants.RetakePassword.CACHE_NAMESPACE);
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "该用户存在", "/retakePassword/confirminfo?" + Constants.UUID.KEY_NAME + "=" + uuid);
			responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "成功，用户存在");
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "用户名不存在", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.USERNAME_ERROR, "用户名不存在");
		}
		responseData.setResponseHeader(responseHeader);
		return responseData;
	}

	/**
	 * 身份认证界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/confirminfo")
	public ModelAndView confirmInfo(HttpServletRequest request) {
		// 缓存中获取账户信息
		String uuid = (String) request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.RetakePassword.CACHE_NAMESPACE, SSOClientUser.class);
		if (userClient == null) {
			return new ModelAndView("redirect:/retakePassword/userinfo");
		}
		// 账户加密数据
		Map<String, Object> model = new HashMap<String, Object>();
		String phone = userClient.getPhone();
		String email = userClient.getEmail();
		AccountData confirmInfo = new AccountData(phone, email);
		model.put("confirmInfo", confirmInfo);
		model.put("uuid", uuid);
		return new ModelAndView("jsp/retakepassword/confirminfo", model);
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
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.RetakePassword.CACHE_NAMESPACE, SSOClientUser.class);
		String checkType = sendVerifyRequest.getCheckType();
		ResponseData<String> responseData = null;
		String sessionId = request.getSession().getId();
		if (userClient != null) {
			if (RetakePassword.CHECK_TYPE_PHONE.equals(checkType)) {
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
			} else if (RetakePassword.CHECK_TYPE_EMAIL.equals(checkType)) {
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
	private String sendPhoneVerifyCode(String sessionId, SSOClientUser userClient) {
		SMDataInfoNotify smDataInfoNotify = new SMDataInfoNotify();
		String phoneVerifyCode = RandomUtil.randomNum(PhoneVerifyConstants.VERIFY_SIZE);
		// 查询是否发送过短信
		String smstimes = "1";
		String smskey = SMSUtil.CACHE_KEY_SMS_RETAKE_PASSWORD + userClient.getPhone();
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(RetakePassword.CACHE_NAMESPACE);
		String times = cacheClient.get(smskey);
		if (StringUtil.isBlank(times)) {
			// 将验证码放入缓存
			String cacheKey = RetakePassword.CACHE_KEY_VERIFY_PHONE + sessionId;
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
			// 已经发送
			return "0002";
		}

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
	 * 身份认证
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkConfirmInfo")
	@ResponseBody
	public ResponseData<String> confirmInfo(HttpServletRequest request, SafetyConfirmData safetyConfirmData) {
		ResponseData<String> responseData = null;
		String confirmType = safetyConfirmData.getConfirmType();
		String sessionId = request.getSession().getId();
		// 检查图片验证码
		String cacheKey = RetakePassword.CACHE_KEY_VERIFY_PICTURE + sessionId;
		String pictureVerifyCode = safetyConfirmData.getPictureVerifyCode();
		ResponseData<String> pictureCheck = checkPictureVerifyCode(pictureVerifyCode, cacheKey);
		String resultCode = pictureCheck.getResponseHeader().getResultCode();
		// 如果失败就直接返回
		if (!VerifyConstants.ResultCodeConstants.SUCCESS_CODE.equals(resultCode)) {
			return pictureCheck;
		}
		// 检查短信或邮箱验证码
		if (RetakePassword.CHECK_TYPE_PHONE.equals(confirmType)) {
			// 检查短信验证码
			String phoneCacheKey = RetakePassword.CACHE_KEY_VERIFY_PHONE + sessionId;
			String verifyCode = safetyConfirmData.getVerifyCode();
			ResponseData<String> phoneCheck = checkPhoneVerifyCode(verifyCode, phoneCacheKey);
			String phoneResultCode = phoneCheck.getResponseHeader().getResultCode();
			if (!VerifyConstants.ResultCodeConstants.SUCCESS_CODE.equals(phoneResultCode)) {
				return phoneCheck;
			}

		} else if (RetakePassword.CHECK_TYPE_EMAIL.equals(confirmType)) {
			// 检查邮箱验证码
			String emailCacheKey = RetakePassword.CACHE_KEY_VERIFY_EMAIL + sessionId;
			String verifyCode = safetyConfirmData.getVerifyCode();
			ResponseData<String> emailCheck = checkEmailVerifyCode(verifyCode, emailCacheKey);
			String emailResultCode = emailCheck.getResponseHeader().getResultCode();
			if (!VerifyConstants.ResultCodeConstants.SUCCESS_CODE.equals(emailResultCode)) {
				return emailCheck;
			}
		}
		// 设置新缓存
		String uuid = (String) request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.RetakePassword.CACHE_NAMESPACE, SSOClientUser.class);
		String newuuid = UUIDUtil.genId32();
		CacheUtil.setValue(newuuid, Constants.UUID.OVERTIME, userClient, Constants.RetakePassword.CACHE_NAMESPACE);
		CacheUtil.deletCache(uuid, Constants.RetakePassword.CACHE_NAMESPACE);
		responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "正确", "/retakePassword/resetPassword?" + Constants.UUID.KEY_NAME + "=" + newuuid);
		ResponseHeader responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "正确");
		responseData.setResponseHeader(responseHeader);
		return responseData;
	}

	/**
	 * 检查图片验证码
	 * 
	 * @param safetyConfirmData
	 * @param cacheClient
	 * @param sessionId
	 * @return
	 */
	private ResponseData<String> checkPictureVerifyCode(String pictureVerifyCode, String cacheKey) {
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(RetakePassword.CACHE_NAMESPACE);
		String pictureVerifyCodeCache = cacheClient.get(cacheKey);
		ResponseData<String> responseData = null;
		ResponseHeader responseHeader = null;
		if (pictureVerifyCodeCache == null) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "图形验证码已失效", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_PICTURE_ERROR, "图形验证码已失效");
		} else if (pictureVerifyCodeCache.compareToIgnoreCase(pictureVerifyCode) != 0) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "图形验证码错误", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_PICTURE_ERROR, "图形验证码错误");
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "图形验证码正确", null);
			responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "图形验证码正确");
		}
		responseData.setResponseHeader(responseHeader);
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
	private ResponseData<String> checkPhoneVerifyCode(String verifyCode, String cacheKey) {
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(RetakePassword.CACHE_NAMESPACE);
		String verifyCodeCache = cacheClient.get(cacheKey);
		ResponseData<String> responseData = null;
		ResponseHeader responseHeader = null;
		if (verifyCodeCache == null) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "验证码已失效", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_VERIFY_ERROR, "验证码已失效");
		} else if (!verifyCodeCache.equals(verifyCode)) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "验证码错误", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_VERIFY_ERROR, "验证码错误");
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "手机校验码正确", null);
			responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "手机校验码正确");
		}
		responseData.setResponseHeader(responseHeader);
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
	private ResponseData<String> checkEmailVerifyCode(String verifyCode, String cacheKey) {
		ICacheClient cacheClient = CacheClientFactory.getCacheClient(RetakePassword.CACHE_NAMESPACE);
		String verifyCodeCache = cacheClient.get(cacheKey);
		ResponseData<String> responseData = null;
		ResponseHeader responseHeader = null;
		if (verifyCodeCache == null) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "验证码已失效", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_VERIFY_ERROR, "验证码已失效");
		} else if (!verifyCodeCache.equals(verifyCode)) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "验证码错误", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_VERIFY_ERROR, "验证码错误");
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "邮箱校验码正确", null);
			responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "邮箱校验码正确");
		}
		responseData.setResponseHeader(responseHeader);
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
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.RetakePassword.CACHE_NAMESPACE, SSOClientUser.class);
		if (userClient == null) {
			return new ModelAndView("redirect:/retakePassword/userinfo");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("uuid", uuid);
		return new ModelAndView("jsp/retakepassword/resetpassword", model);
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
		ResponseHeader responseHeader = null;
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.RetakePassword.CACHE_NAMESPACE, SSOClientUser.class);
		if (userClient == null) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "认证身份失效", "/retakePassword/userinfo");
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "认证身份失效");
		} else {
			IAccountSecurityManageSV accountManageSV = DubboConsumerFactory.getService("iAccountSecurityManageSV");
			AccountPasswordRequest passwordRequest = new AccountPasswordRequest();
			passwordRequest.setAccountId(userClient.getAccountId());
			String encodePassword = Md5Encoder.encodePassword(password);
			passwordRequest.setAccountPassword(encodePassword);
			passwordRequest.setUpdateAccountId(userClient.getAccountId());
			BaseResponse resultData = accountManageSV.setPasswordData(passwordRequest);
			if (ResultCode.SUCCESS_CODE.equals(resultData.getResponseHeader().getResultCode())) {
				String newuuid = UUIDUtil.genId32();
				CacheUtil.setValue(newuuid, Constants.UUID.OVERTIME, userClient, Constants.RetakePassword.CACHE_NAMESPACE);
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "重置密码成功", "/retakePassword/success?" + Constants.UUID.KEY_NAME + "=" + newuuid);
				responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "重置密码成功");
				// 删除缓存
				CacheUtil.deletCache(uuid, Constants.RetakePassword.CACHE_NAMESPACE);
			} else {
				String resultMessage = resultData.getResponseHeader().getResultMessage();
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, resultMessage, resultMessage);
				responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.ERROR_CODE, "重置密码失败");
			}
		}
		responseData.setResponseHeader(responseHeader);
		return responseData;
	}

	@RequestMapping("/success")
	public ModelAndView successPage(HttpServletRequest request) {
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.RetakePassword.CACHE_NAMESPACE, SSOClientUser.class);
		if (userClient == null) {
			return new ModelAndView("redirect:/retakePassword/userinfo");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("uuid", uuid);
		// CacheUtil.deletCache(uuid, Constants.RetakePassword.CACHE_NAMESPACE);
		return new ModelAndView("jsp/retakepassword/retaksuccess", model);
	}

	@RequestMapping("/login")
	@ResponseBody
	public ResponseData<String> autoLogin(HttpServletRequest request, HttpServletResponse response) {
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.RetakePassword.CACHE_NAMESPACE, SSOClientUser.class);
		// 删除缓存
		if (userClient == null) {
			// 跳转到登录页面
			String casServerLoginUrlRuntime = SSOClientUtil.getCasServerLoginUrlRuntime(request);
			ResponseData<String> responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "认证失效", casServerLoginUrlRuntime);
			ResponseHeader responseHeader = new ResponseHeader(false, Constants.RetakePassword.FAIL_CODE, null);
			responseData.setResponseHeader(responseHeader);
			return responseData;
		}
		CacheUtil.deletCache(uuid, Constants.RetakePassword.CACHE_NAMESPACE);
		ILoginSV iloginSV = DubboConsumerFactory.getService("iLoginSV");
		UserLoginResponse account = iloginSV.queryAccountByUserName(userClient.getPhone());
		String phone = account.getPhone();
		String accountPassword = account.getAccountPassword();
		LoginUser loginUser = new LoginUser(phone, accountPassword);
		String newuuid = UUIDUtil.genId32();
		CacheUtil.setValue(newuuid, Constants.UUID.OVERTIME, loginUser, Constants.LoginConstant.CACHE_NAMESPACE);
		// localhost:8080/uac/registerLogin?k=UUID&service=URL
		String service_url = "";
		if (StringUtil.isBlank(uuid)) {
			// 跳转到登录页面
			service_url = SSOClientUtil.getCasServerLoginUrlRuntime(request);
		} else {
			// 从配置中心读取跳转地址
			service_url = ConfigCenterFactory.getConfigCenterClient().get(Constants.URLConstant.INDEX_URL_KEY);
		}
		String url = "/registerLogin?" + Constants.UUID.KEY_NAME + "=" + newuuid + "&service=" + service_url;
		ResponseData<String> responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "成功，跳转", url);
		ResponseHeader responseHeader = new ResponseHeader(true, Constants.RetakePassword.SUCCESS_CODE, null);
		responseData.setResponseHeader(responseHeader);
		return responseData;
	}
}
