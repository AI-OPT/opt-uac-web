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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ai.opt.base.vo.BaseResponse;
import com.ai.opt.base.vo.ResponseHeader;
import com.ai.opt.sdk.components.ccs.CCSClientFactory;
import com.ai.opt.sdk.components.mcs.MCSClientFactory;
import com.ai.opt.sdk.dubbo.util.DubboConsumerFactory;
import com.ai.opt.sdk.util.RandomUtil;
import com.ai.opt.sdk.util.StringUtil;
import com.ai.opt.sdk.util.UUIDUtil;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.sso.client.filter.SSOClientConstants;
import com.ai.opt.sso.client.filter.SSOClientUser;
import com.ai.opt.uac.api.security.interfaces.IAccountSecurityManageSV;
import com.ai.opt.uac.api.security.param.AccountPhoneRequest;
import com.ai.opt.uac.web.constants.Constants;
import com.ai.opt.uac.web.constants.Constants.ResultCode;
import com.ai.opt.uac.web.constants.Constants.UpdatePhone;
import com.ai.opt.uac.web.constants.VerifyConstants;
import com.ai.opt.uac.web.constants.VerifyConstants.EmailVerifyConstants;
import com.ai.opt.uac.web.constants.VerifyConstants.PhoneVerifyConstants;
import com.ai.opt.uac.web.constants.VerifyConstants.ResultCodeConstants;
import com.ai.opt.uac.web.model.email.SendEmailRequest;
import com.ai.opt.uac.web.model.retakepassword.AccountData;
import com.ai.opt.uac.web.model.retakepassword.SafetyConfirmData;
import com.ai.opt.uac.web.util.CacheUtil;
import com.ai.opt.uac.web.util.IPUtil;
import com.ai.opt.uac.web.util.VerifyUtil;
import com.ai.paas.ipaas.ccs.IConfigClient;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;
import com.ai.runner.center.mmp.api.manager.param.SMData;
import com.ai.runner.center.mmp.api.manager.param.SMDataInfoNotify;

@RequestMapping("/center/phone")
@Controller
public class UpdatePhoneController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePhoneController.class);

	@RequestMapping("/confirminfo")
	public ModelAndView UpdatePhoneStart(HttpServletRequest request) {
		SSOClientUser userClient = (SSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
		if (userClient != null) {
			Map<String, AccountData> model = new HashMap<String, AccountData>();
			String phone = userClient.getPhone();
			String email = userClient.getEmail();
			AccountData confirmInfo = new AccountData(phone, email);
			model.put("confirmInfo", confirmInfo);
			return new ModelAndView("jsp/center/update-phone-start", model);
		} else {
			return new ModelAndView("jsp/center/update-phone-start");
		}
	}

	@RequestMapping("/getImageVerifyCode")
	@ResponseBody
	public void getImageVerifyCode(HttpServletRequest request, HttpServletResponse response) {
		String cacheKey = UpdatePhone.CACHE_KEY_VERIFY_PICTURE + request.getSession().getId();
		BufferedImage image = VerifyUtil.getImageVerifyCode(UpdatePhone.CACHE_NAMESPACE, cacheKey, 100, 38);
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
	public ResponseData<String> sendVerify(HttpServletRequest request, @RequestParam(value="confirmType",required=false)String confirmType) {
		SSOClientUser userClient = (SSOClientUser) request.getSession().getAttribute(SSOClientConstants.USER_SESSION_KEY);
		ResponseData<String> responseData = null;
		String sessionId = request.getSession().getId();
		IConfigClient defaultConfigClient = CCSClientFactory.getDefaultConfigClient();
		try{
		    if (userClient != null) {
	            if (UpdatePhone.CHECK_TYPE_PHONE.equals(confirmType)) {
	                // 检查ip发送次数
	                ResponseData<String> checkIpSendPhone = VerifyUtil.checkIPSendPhoneCount(UpdatePhone.CACHE_NAMESPACE, IPUtil.getIp(request)+UpdatePhone.CACHE_KEY_IP_SEND_PHONE_NUM);
	                if(!checkIpSendPhone.getResponseHeader().isSuccess()){
	                    return checkIpSendPhone;
	                }
	                // 发送手机验证码
	                String isSuccess = sendPhoneVerifyCode(sessionId, userClient);
	                if ("0000".equals(isSuccess)) {
	                    responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "短信验证码发送成功", "短信验证码发送成功");
	                    ResponseHeader header = new ResponseHeader();
	                    header.setIsSuccess(true);
	                    header.setResultCode(ResultCodeConstants.SUCCESS_CODE);
	                    responseData.setResponseHeader(header);
	                    return responseData;
	                } else if ("0002".equals(isSuccess)) {
	                    String maxTimeStr = defaultConfigClient.get(PhoneVerifyConstants.SEND_VERIFY_MAX_TIME_KEY);
	                    String errorMsg = Integer.valueOf(maxTimeStr) / 60 + "分钟内不可重复发送";
	                    responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, errorMsg, errorMsg);
	                    ResponseHeader header = new ResponseHeader();
	                    header.setIsSuccess(false);
	                    header.setResultCode(ResultCodeConstants.REGISTER_VERIFY_ERROR);
	                    header.setResultMessage(errorMsg);
	                    responseData.setResponseHeader(header);
	                    return responseData;
	                } else {
	                    responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "短信验证码发送失败", "服务器连接超时");
	                    ResponseHeader header = new ResponseHeader();
	                    header.setIsSuccess(false);
	                    header.setResultCode(ResultCodeConstants.ERROR_CODE);
	                    responseData.setResponseHeader(header);
	                    return responseData;
	                }

	            } else if (UpdatePhone.CHECK_TYPE_EMAIL.equals(confirmType)) {
	                // 检查ip发送次数
	                ResponseData<String> checkIpSendEmail = VerifyUtil.checkIPSendEmailCount(UpdatePhone.CACHE_NAMESPACE, IPUtil.getIp(request)+UpdatePhone.CACHE_KEY_IP_SEND_EMAIL_NUM);
	                if(!checkIpSendEmail.getResponseHeader().isSuccess()){
	                    return checkIpSendEmail;
	                }
	                // 发送邮件验证码
	                String isSuccess = sendEmailVerifyCode(sessionId, userClient);

	                if ("0000".equals(isSuccess)) {
	                    responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "短信验证码发送成功", null);
	                    ResponseHeader header = new ResponseHeader(true, ResultCodeConstants.SUCCESS_CODE, "短信验证码发送成功");
	                    responseData.setResponseHeader(header);
	                    return responseData;
	                } else if ("0002".equals(isSuccess)) {
	                    String maxTimeStr = defaultConfigClient.get(EmailVerifyConstants.SEND_VERIFY_MAX_TIME_KEY);
	                    String errorMsg = Integer.valueOf(maxTimeStr) / 60 + "分钟内不可重复发送";
	                    responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, errorMsg, null);
	                    ResponseHeader header = new ResponseHeader();
	                    header.setIsSuccess(false);
	                    header.setResultCode(ResultCodeConstants.REGISTER_VERIFY_ERROR);
	                    header.setResultMessage(errorMsg);
	                    responseData.setResponseHeader(header);
	                    return responseData;
	                } else {
	                    responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "短信验证码发送失败", null);
	                    ResponseHeader header = new ResponseHeader();
	                    header.setIsSuccess(false);
	                    header.setResultCode(ResultCodeConstants.ERROR_CODE);
	                    responseData.setResponseHeader(header);
	                    return responseData;
	                }
	            } else {
	                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "验证码发送失败", null);
	                ResponseHeader responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.ERROR_CODE, "验证码发送失败");
	                responseData.setResponseHeader(responseHeader);
	                return responseData;
	            }
	        } else {
	            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "认证信息失败", "/center/phone/confirminfo");
	            ResponseHeader responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.USER_INFO_NULL, "认证信息失败");
	            responseData.setResponseHeader(responseHeader);
	            return responseData;
	        }
		}catch(Exception e){
		    LOGGER.error("发送验证码错误：" + e);
		}
		return null;
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
		String smskey = UpdatePhone.CACHE_KEY_CONFIRM_SEND_PHONE_NUM + userClient.getPhone();
		ICacheClient cacheClient = MCSClientFactory.getCacheClient(UpdatePhone.CACHE_NAMESPACE);
		IConfigClient defaultConfigClient = CCSClientFactory.getDefaultConfigClient();
		String times = cacheClient.get(smskey);
		try{
		    if (StringUtil.isBlank(times)) {
	            // 将验证码放入缓存
	            String cacheKey = UpdatePhone.CACHE_KEY_VERIFY_PHONE + sessionId;
	            String overTimeStr = defaultConfigClient.get(PhoneVerifyConstants.VERIFY_OVERTIME_KEY);
	            cacheClient.setex(cacheKey, Integer.valueOf(overTimeStr), phoneVerifyCode);
	            // 将发送次数放入缓存
	            String maxTimeStr = defaultConfigClient.get(PhoneVerifyConstants.SEND_VERIFY_MAX_TIME_KEY);
	            cacheClient.setex(smskey, Integer.valueOf(maxTimeStr), smstimes);
	            // 设置短息信息
	            List<SMData> dataList = new LinkedList<SMData>();
	            SMData smData = new SMData();
	            smData.setGsmContent("${VERIFY}:" + phoneVerifyCode + "^${VALIDMINS}:" + Integer.valueOf(overTimeStr) / 60);
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
	            // 已发送
	            return "0002";
	        }
		}catch(Exception e){
		    LOGGER.error("发送验证码错误：" + e);
		}
		return null;

	}

	/**
	 * 发送邮件验证码
	 * 
	 * @param accountInfo
	 */
	private String sendEmailVerifyCode(String sessionId, SSOClientUser userClient) {

		// 查询是否发送过短信
		String smstimes = "1";
		String smskey = UpdatePhone.CACHE_KEY_CONFIRM_SEND_EMAIL_NUM + userClient.getPhone();
		ICacheClient cacheClient = MCSClientFactory.getCacheClient(UpdatePhone.CACHE_NAMESPACE);
		IConfigClient defaultConfigClient = CCSClientFactory.getDefaultConfigClient();
		String times = cacheClient.get(smskey);
		try{
		    if (StringUtil.isBlank(times)) {
	            // 邮箱验证
	            String email = userClient.getEmail();
	            String nickName = userClient.getNickName();
	            SendEmailRequest emailRequest = new SendEmailRequest();
	            emailRequest.setTomails(new String[] { email });
	            emailRequest.setTemplateRUL(UpdatePhone.TEMPLATE_EMAIL_URL);
	            emailRequest.setSubject(UpdatePhone.EMAIL_SUBJECT);
	            // 验证码
	            String verifyCode = RandomUtil.randomNum(EmailVerifyConstants.VERIFY_SIZE);
	            // 将验证码放入缓存
	            String cacheKey = UpdatePhone.CACHE_KEY_VERIFY_EMAIL + sessionId;
	            String overTimeStr = defaultConfigClient.get(EmailVerifyConstants.VERIFY_OVERTIME_KEY);
	            cacheClient.setex(cacheKey, Integer.valueOf(overTimeStr), verifyCode);
	            // 将发送次数放入缓存
	            String maxTimeStr = defaultConfigClient.get(EmailVerifyConstants.SEND_VERIFY_MAX_TIME_KEY);
	            cacheClient.setex(smskey, Integer.valueOf(maxTimeStr), smstimes);
	            // 超时时间
	            String overTime = ObjectUtils.toString(Integer.valueOf(overTimeStr) / 60);
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

		}catch(Exception e){
		    LOGGER.error("发送验证码错误：" + e);
		}
		return null;
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
		ICacheClient cacheClient = MCSClientFactory.getCacheClient(UpdatePhone.CACHE_NAMESPACE);
		String sessionId = request.getSession().getId();
		// 检查图片验证码
		String pictureVerifyCodeCache = cacheClient.get(UpdatePhone.CACHE_KEY_VERIFY_PICTURE + sessionId);
		String pictureVerifyCode = safetyConfirmData.getPictureVerifyCode();
		ResponseData<String> pictureCheck = VerifyUtil.checkPictureVerifyCode(pictureVerifyCode, pictureVerifyCodeCache);
		String resultCode = pictureCheck.getResponseHeader().getResultCode();
		if (!VerifyConstants.ResultCodeConstants.SUCCESS_CODE.equals(resultCode)) {
			return pictureCheck;
		}
		// 检查短信或邮箱验证码
		if (UpdatePhone.CHECK_TYPE_PHONE.equals(confirmType)) {
			// 检查短信验证码
			String verifyCodeCache = cacheClient.get(UpdatePhone.CACHE_KEY_VERIFY_PHONE + sessionId);
			String verifyCode = safetyConfirmData.getVerifyCode();
			ResponseData<String> phoneCheck = VerifyUtil.checkPhoneVerifyCode(verifyCode, verifyCodeCache);
			String phoneResultCode = phoneCheck.getResponseHeader().getResultCode();
			if (!VerifyConstants.ResultCodeConstants.SUCCESS_CODE.equals(phoneResultCode)) {
				return phoneCheck;
			}
		} else if (UpdatePhone.CHECK_TYPE_EMAIL.equals(confirmType)) {
			// 检查邮箱验证码
			String verifyCodeCache = cacheClient.get(UpdatePhone.CACHE_KEY_VERIFY_EMAIL + sessionId);
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
		CacheUtil.setValue(uuid, Constants.UUID.OVERTIME, userClient, Constants.UpdatePhone.CACHE_NAMESPACE);
		responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "正确", "/center/phone/setPhone?" + Constants.UUID.KEY_NAME + "=" + uuid);
		ResponseHeader responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "正确");
		responseData.setResponseHeader(responseHeader);
		return responseData;
	}

	/**
	 * 修改手机页跳转
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/setPhone")
	public ModelAndView UpdatePhonePage(HttpServletRequest request) {
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.UpdatePhone.CACHE_NAMESPACE, SSOClientUser.class);
		if (userClient == null) {
			return new ModelAndView("redirect:/center/phone/confirminfo");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("uuid", uuid);
		return new ModelAndView("jsp/center/update-phone-new", model);
	}
	
	/**
	 * 检查修改手机号正确性（不与原手机不同，不重复）
	 * @param request
	 * @param phone
	 * @return
	 */
	@RequestMapping("/checkPhoneValue")
	@ResponseBody
	public ResponseData<String> checkPhoneDiffOld(HttpServletRequest request, String phone){
		ResponseData<String> responseData = null;
		ResponseHeader responseHeader = null;
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.UpdateEmail.CACHE_NAMESPACE, SSOClientUser.class);
		if (userClient == null) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "身份认证失效", "/center/phone/confirminfo");
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.USER_INFO_NULL, "认证身份失效");
			responseData.setResponseHeader(responseHeader);
			return responseData;
		}
		//检查不用原手机号相同
		String oldPhone = userClient.getPhone();
		if (phone.equals(oldPhone)) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "新手机号码不能与旧手机号码相同，请重新输入", null);
			responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.PHONE_ERROR, "新手机号码不能与旧手机号码相同，请重新输入");
			responseData.setResponseHeader(responseHeader);
			return responseData;
		}
		//检查手机号码唯一性
		return VerifyUtil.checkPhoneOnly(phone);
	}
	
	/**
	 * 发送短信验证码(修改新手机时验证)
	 * 
	 * @param request
	 * @param sessionId
	 * @param email
	 * @return
	 */
	@RequestMapping("/sendPhoneVerify")
	@ResponseBody
	public ResponseData<String> sendPhoneVerifyCode(HttpServletRequest request, String phone) {
		ResponseData<String> responseData = null;
		ResponseHeader responseHeader = null;
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.UpdatePhone.CACHE_NAMESPACE, SSOClientUser.class);
		IConfigClient defaultConfigClient = CCSClientFactory.getDefaultConfigClient();
		try{
		    if (userClient == null) {
	            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "身份认证失效", "/center/phone/confirminfo");
	            responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.USER_INFO_NULL, "认证身份失效");
	            responseData.setResponseHeader(responseHeader);
	            return responseData;
	        } else {
	            // 检查ip发送次数
	            ResponseData<String> checkIpSendPhone = VerifyUtil.checkIPSendPhoneCount(UpdatePhone.CACHE_NAMESPACE, IPUtil.getIp(request)+UpdatePhone.CACHE_KEY_IP_SEND_PHONE_NUM);
	            if(!checkIpSendPhone.getResponseHeader().isSuccess()){
	                return checkIpSendPhone;
	            }
	            String rasultCode = sendUpdatePhoneVerifyCode(request, phone, userClient);
	            if ("0000".equals(rasultCode)) {
	                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "短信验证码发送成功", "短信验证码发送成功");
	                ResponseHeader header = new ResponseHeader();
	                header.setIsSuccess(true);
	                header.setResultCode(ResultCodeConstants.SUCCESS_CODE);
	                responseData.setResponseHeader(header);
	                return responseData;
	            } else if ("0002".equals(rasultCode)) {
	                String maxTimeStr = defaultConfigClient.get(PhoneVerifyConstants.SEND_VERIFY_MAX_TIME_KEY);
	                String errorMsg = Integer.valueOf(maxTimeStr)/60+"分钟内不可重复发送";
	                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, errorMsg, errorMsg);
	                ResponseHeader header = new ResponseHeader();
	                header.setIsSuccess(false);
	                header.setResultCode(ResultCodeConstants.REGISTER_VERIFY_ERROR);
	                header.setResultMessage(errorMsg);
	                responseData.setResponseHeader(header);
	                return responseData;
	            } else {
	                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "短信验证码发送失败", "服务器连接超时");
	                ResponseHeader header = new ResponseHeader();
	                header.setIsSuccess(false);
	                header.setResultCode(ResultCodeConstants.ERROR_CODE);
	                responseData.setResponseHeader(header);
	                return responseData;
	            }
	        }
		}catch(Exception e){
		    LOGGER.error("发送验证码错误：" + e);
		}
		return null;
	}

	private String sendUpdatePhoneVerifyCode(HttpServletRequest request, String phone, SSOClientUser userClient) {
		// 查询是否发送过邮件
		String smstimes = "1";
		String smskey = UpdatePhone.CACHE_KEY_UPDATE_SEND_PHONE_NUM + phone + request.getSession().getId();
		ICacheClient cacheClient = MCSClientFactory.getCacheClient(UpdatePhone.CACHE_NAMESPACE);
		IConfigClient defaultConfigClient = CCSClientFactory.getDefaultConfigClient();
		String times = cacheClient.get(smskey);
		try{
		    if (StringUtil.isBlank(times)) {
	            SMDataInfoNotify smDataInfoNotify = new SMDataInfoNotify();
	            String phoneVerifyCode = RandomUtil.randomNum(PhoneVerifyConstants.VERIFY_SIZE);
	            // 将验证码放入缓存
	            String cacheKey = UpdatePhone.CACHE_KEY_VERIFY_SETPHONE + phone + request.getSession().getId();
	            String overTimeStr = defaultConfigClient.get(PhoneVerifyConstants.VERIFY_OVERTIME_KEY);
	            cacheClient.setex(cacheKey, Integer.valueOf(overTimeStr), phoneVerifyCode);
	            // 将发送次数放入缓存
	            String maxTimeStr = defaultConfigClient.get(PhoneVerifyConstants.SEND_VERIFY_MAX_TIME_KEY);
	            cacheClient.setex(smskey, Integer.valueOf(maxTimeStr), smstimes);
	            // 设置短息信息
	            List<SMData> dataList = new LinkedList<SMData>();
	            SMData smData = new SMData();
	            smData.setGsmContent("${VERIFY}:" + phoneVerifyCode + "^${VALIDMINS}:" + Integer.valueOf(overTimeStr) / 60);
	            smData.setPhone(phone);
	            smData.setTemplateId(PhoneVerifyConstants.TEMPLATE_RETAKE_SETPHONE_ID);
	            smData.setServiceType(PhoneVerifyConstants.SERVICE_TYPE);
	            dataList.add(smData);
	            smDataInfoNotify.setDataList(dataList);
	            smDataInfoNotify.setMsgSeq(VerifyUtil.createPhoneMsgSeq());
	            smDataInfoNotify.setTenantId(userClient.getTenantId());
	            smDataInfoNotify.setSystemId(Constants.SYSTEM_ID);
	            boolean isSuccess = VerifyUtil.sendPhoneInfo(smDataInfoNotify);
	            if (isSuccess) {
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
		}catch(Exception e){
		    LOGGER.error("发送验证码错误：" + e);
		}
		return null;
	}

	/**
	 * 设置新手机号
	 * 
	 * @param request
	 * @param newPassword
	 * @return
	 */
	@RequestMapping("/setNewPhone")
	@ResponseBody
	public ResponseData<String> setNewPhone(HttpServletRequest request, String phone, String verifyCode) {
		ResponseData<String> responseData = null;
		ResponseHeader responseHeader = null;
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.UpdatePhone.CACHE_NAMESPACE, SSOClientUser.class);
		if (userClient == null) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "身份认证失效", "/center/phone/confirminfo");
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.USER_INFO_NULL, "认证身份失效");
			responseData.setResponseHeader(responseHeader);
		} else {
			String oldPhone = userClient.getPhone();
			if (phone.equals(oldPhone)) {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "新手机号码不能与旧手机号码相同，请重新输入", null);
				responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.PHONE_ERROR, "新手机号码不能与旧手机号码相同，请重新输入");
				responseData.setResponseHeader(responseHeader);
				return responseData;
			}
			// 检查验证码
			ICacheClient cacheClient = MCSClientFactory.getCacheClient(UpdatePhone.CACHE_NAMESPACE);
			String cacheKey = UpdatePhone.CACHE_KEY_VERIFY_SETPHONE + phone + request.getSession().getId();
			String verifyCodeCache = cacheClient.get(cacheKey);
			ResponseData<String> checkVerifyCode = VerifyUtil.checkPhoneVerifyCode(verifyCode, verifyCodeCache);
			String phoneResultCode = checkVerifyCode.getResponseHeader().getResultCode();
			if (!VerifyConstants.ResultCodeConstants.SUCCESS_CODE.equals(phoneResultCode)) {
				responseData = checkVerifyCode;
			} else {
				// 更新手机
				IAccountSecurityManageSV accountSecurityManageSV = DubboConsumerFactory.getService("iAccountSecurityManageSV");
				AccountPhoneRequest accountPhoneRequest = new AccountPhoneRequest();
				accountPhoneRequest.setAccountId(userClient.getAccountId());
				accountPhoneRequest.setPhone(phone);
				accountPhoneRequest.setUpdateAccountId(userClient.getAccountId());
				BaseResponse resultData = accountSecurityManageSV.setPhoneData(accountPhoneRequest);
				if (ResultCode.SUCCESS_CODE.equals(resultData.getResponseHeader().getResultCode())) {
					String newuuid = UUIDUtil.genId32();
					userClient.setPhone(phone);// 更改为新手机号
					CacheUtil.setValue(newuuid, Constants.UUID.OVERTIME, userClient, Constants.UpdatePhone.CACHE_NAMESPACE);
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "修改手机成功", "/center/phone/success?" + Constants.UUID.KEY_NAME + "=" + newuuid);
					responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "修改手机成功");
					responseData.setResponseHeader(responseHeader);
					CacheUtil.deletCache(uuid, Constants.UpdatePhone.CACHE_NAMESPACE);
				} else if (ResultCode.PHONE_NOTONE_ERROR.equals(resultData.getResponseHeader().getResultCode())) {
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "该手机号码已经被注册，请使用其它手机号码", null);
					responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.PHONE_ERROR, "该手机号码已经被注册，请使用其它手机号码");
					responseData.setResponseHeader(responseHeader);
				} else {
					String resultMessage = resultData.getResponseHeader().getResultMessage();
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, resultMessage, null);
					responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "修改手机失败");
					responseData.setResponseHeader(responseHeader);
				}
			}
		}
		return responseData;
	}

	@RequestMapping("/success")
	public ModelAndView successPage(HttpServletRequest request) {
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		SSOClientUser userClient = (SSOClientUser) CacheUtil.getValue(uuid, Constants.UpdatePhone.CACHE_NAMESPACE, SSOClientUser.class);
		if (userClient == null) {
			return new ModelAndView("redirect:/center/phone/confirminfo");
		}
		request.getSession().setAttribute(SSOClientConstants.USER_SESSION_KEY, userClient);
		CacheUtil.deletCache(uuid, Constants.UpdatePhone.CACHE_NAMESPACE);
		return new ModelAndView("jsp/center/update-phone-success");
	}
}
