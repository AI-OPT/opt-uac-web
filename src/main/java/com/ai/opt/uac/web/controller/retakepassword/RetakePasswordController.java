package com.ai.opt.uac.web.controller.retakepassword;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ai.opt.base.exception.RPCSystemException;
import com.ai.opt.base.vo.BaseResponse;
import com.ai.opt.base.vo.ResponseHeader;
import com.ai.opt.sdk.cache.factory.CacheClientFactory;
import com.ai.opt.sdk.util.DubboConsumerFactory;
import com.ai.opt.sdk.util.Md5Encoder;
import com.ai.opt.sdk.util.RandomUtil;
import com.ai.opt.sdk.util.StringUtil;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.uac.api.account.interfaces.IAccountManageSV;
import com.ai.opt.uac.api.account.param.AccountQueryRequest;
import com.ai.opt.uac.api.account.param.AccountQueryResponse;
import com.ai.opt.uac.api.security.interfaces.IAccountSecurityManageSV;
import com.ai.opt.uac.api.security.param.AccountPasswordRequest;
import com.ai.opt.uac.web.constants.Constants.ResultCode;
import com.ai.opt.uac.web.constants.Constants.RetakePassword;
import com.ai.opt.uac.web.constants.Constants.VerifyCode;
import com.ai.opt.uac.web.model.email.SendEmailRequest;
import com.ai.opt.uac.web.model.retakepassword.AccountData;
import com.ai.opt.uac.web.model.retakepassword.SafetyConfirmData;
import com.ai.opt.uac.web.model.retakepassword.SendVerifyRequest;
import com.ai.opt.uac.web.util.EmailUtil;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;

@RequestMapping("/retakePassword")
@Controller
public class RetakePasswordController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RetakePasswordController.class);

	/**
	 * 身份认证界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/confirminfo")
	public ModelAndView retakePassPhone(HttpServletRequest request) {
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
		// Long accountId =
		// (Long)request.getSession().getAttribute("accountId");
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

	/**
	 * 发送邮件
	 * 
	 * @return
	 */
	@RequestMapping("/sendVerify")
	@ResponseBody
	public ResponseData<String> sendVerify(HttpServletRequest request, SendVerifyRequest sendVerifyRequest) {
		String checkType = sendVerifyRequest.getCheckType();
		ResponseData<String> responseData = null;
		AccountQueryResponse accountInfo = getAccountInfoById(sendVerifyRequest.getAccountId());
		if (accountInfo != null) {
			if (RetakePassword.CHECK_TYPE_PHONE.equals(checkType)) {
				// 手机验证
				String phone = accountInfo.getPhone();
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "发送成功", "发送成功");
			} else if (RetakePassword.CHECK_TYPE_EMAIL.equals(checkType)) {
				// 邮箱验证
				String email = accountInfo.getEmail();
				String nickName = accountInfo.getNickName();
				SendEmailRequest emailRequest = new SendEmailRequest();
				emailRequest.setTomails(new String[] { email });
				emailRequest.setTemplateRUL(RetakePassword.TEMPLATE_EMAIL_URL);
				// 验证码
				String verifyCode = RandomUtil.randomNum(VerifyCode.VERIFY_SIZE_EMAIL);
				// 将验证码放入缓存
				ICacheClient cacheClient = CacheClientFactory.getCacheClient(RetakePassword.CACHE_NAMESPACE);
				String cacheKey = RetakePassword.CACHE_KEY_VERIFY_EMAIL+request.getSession().getId();
				cacheClient.setex(cacheKey, VerifyCode.VERIFY_OVERTIME_EMAIL, verifyCode);

				String value = cacheClient.get(cacheKey);
				System.out.println("************************" + value);
				// 超时时间
				String overTime = ObjectUtils.toString(VerifyCode.VERIFY_OVERTIME_EMAIL / 60);
				emailRequest.setData(new String[] { nickName, verifyCode, overTime });
				EmailUtil.sendEmail(emailRequest);
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "发送成功", "发送成功");
			} else {
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "发送失败", "验证方式不正确");
			}
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "发送失败", "该账号不存在");
		}
		return responseData;
	}

	/**
	 * 获得账户信息
	 * 
	 * @param accountId
	 */
	private AccountQueryResponse getAccountInfoById(Long accountId) {
		AccountQueryResponse accountQueryResponse = null;
		try {
			AccountQueryRequest accountQueryRequest = new AccountQueryRequest();
			accountQueryRequest.setAccountId(accountId);
			IAccountManageSV accountManageSV = DubboConsumerFactory.getService("iAccountManageSV");
			accountQueryResponse = accountManageSV.queryBaseInfo(accountQueryRequest);
		} catch (RPCSystemException e) {
			LOGGER.error("查询失败！", e);
			e.printStackTrace();
		}
		return accountQueryResponse;
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
		// TODO 待处理 验证信息
		String confirmType = safetyConfirmData.getConfirmType();
		ResponseData<String> responseData = null;
		if(RetakePassword.CHECK_TYPE_PHONE.equals(confirmType)){
			
		}else if(RetakePassword.CHECK_TYPE_EMAIL.equals(confirmType)){
			String cacheKey = RetakePassword.CACHE_KEY_VERIFY_EMAIL+request.getSession().getId();
			ICacheClient cacheClient = CacheClientFactory.getCacheClient(RetakePassword.CACHE_NAMESPACE);
			String verifyCodeCache = cacheClient.get(cacheKey);
			String verifyCode = safetyConfirmData.getVerifyCode();
			
			if(verifyCodeCache !=null){
				if(!verifyCodeCache.equals(verifyCode)){
					responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "邮箱校验码已失效", "邮箱校验码已失效");
				}
			}else{
				responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "邮箱校验码已失效", "邮箱校验码已失效");
			}
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
		try {
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
		} catch (RPCSystemException e) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "重置密码失败", null);
			e.printStackTrace();
		}
		return responseData;
	}

	@RequestMapping("/success")
	public ModelAndView successPage() {
		return new ModelAndView("jsp/retakepassword/retaksuccess");
	}

}
