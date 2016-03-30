package com.ai.opt.uac.web.controller.register;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ai.net.xss.util.StringUtil;
import com.ai.opt.base.vo.BaseResponse;
import com.ai.opt.base.vo.ResponseHeader;
import com.ai.opt.sdk.cache.factory.CacheClientFactory;
import com.ai.opt.sdk.util.DubboConsumerFactory;
import com.ai.opt.sdk.util.RandomUtil;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.uac.api.account.interfaces.IAccountManageSV;
import com.ai.opt.uac.api.account.param.AccountQueryRequest;
import com.ai.opt.uac.api.account.param.AccountQueryResponse;
import com.ai.opt.uac.api.register.interfaces.IRegisterSV;
import com.ai.opt.uac.api.register.param.PhoneRegisterRequest;
import com.ai.opt.uac.api.register.param.PhoneRegisterResponse;
import com.ai.opt.uac.api.security.interfaces.IAccountSecurityManageSV;
import com.ai.opt.uac.api.security.param.AccountEmailRequest;
import com.ai.opt.uac.web.constants.Constants;
import com.ai.opt.uac.web.constants.Constants.Register;
import com.ai.opt.uac.web.constants.Constants.ResultCode;
import com.ai.opt.uac.web.constants.VerifyConstants.EmailVerifyConstants;
import com.ai.opt.uac.web.constants.VerifyConstants.PhoneVerifyConstants;
import com.ai.opt.uac.web.model.email.SendEmailRequest;
import com.ai.opt.uac.web.model.register.GetSMDataReq;
import com.ai.opt.uac.web.model.register.UpdateEmailReq;
import com.ai.opt.uac.web.util.Md5Util;
import com.ai.opt.uac.web.util.VerifyUtil;
import com.ai.paas.ipaas.mcs.interfaces.ICacheClient;
import com.ai.runner.base.exception.CallerException;
import com.ai.runner.center.mmp.api.manager.interfaces.SMSServices;
import com.ai.runner.center.mmp.api.manager.param.SMData;
import com.ai.runner.center.mmp.api.manager.param.SMDataInfoNotify;

@RequestMapping("/reg")
@Controller
public class RegisterController {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

    @RequestMapping("/toRegister")
    public ModelAndView register(HttpServletRequest request) {

        return new ModelAndView("jsp/register/register");
    }

    @RequestMapping("/toRegisterEmail")
    public ModelAndView registerEmail(
            @RequestParam(value = "accountId", required = false) String accountId,
            HttpServletRequest request) {
        request.setAttribute("accountId", accountId);
        return new ModelAndView("jsp/register/register-email");
    }

    @RequestMapping("/toRegisterSuccess")
    public ModelAndView registerSuccess(HttpServletRequest request) {

        return new ModelAndView("jsp/register/register-success");
    }

    /**
     * 注册账号
     * 
     * @param request
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public ResponseData<String> addAccount(PhoneRegisterRequest request, HttpSession session) {
        ResponseData<String> responseData = null;
        // MD5加密
        request.setAccountPassword(Md5Util.stringMD5(request.getAccountPassword()));
        try {
         
            ICacheClient iCacheClient = CacheClientFactory.getCacheClient(Register.CACHE_NAMESPACE);
            String pictureCode = iCacheClient.get(Register.CACHE_KEY_VERIFY_PICTURE+session.getId());
            ResponseHeader header = new ResponseHeader();
            header.setIsSuccess(true);
            //校验图片是否失效
            if(StringUtil.isBlank(pictureCode)){
                header.setResultCode(Register.REGISTER_PICTURE_OVERTIME_ERROR);
                header.setResultMessage("图形验证码已失效");
                 responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS,
                        "图形验证码已失效", null);
                 responseData.setResponseHeader(header);
                 return responseData;
            }
          // 校验验证码
            if (!request.getPictureVerifyCode().equals(pictureCode)) {
                header.setResultCode(Register.REGISTER_PICTURE_ERROR);
                header.setResultMessage("图形验证码错误");
                 responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS,
                        "图形验证码错误", null);
                responseData.setResponseHeader(header);
                return responseData;
            }
            
            // 校验短信验证码是否失效
            String phoneCode = iCacheClient.get(Register.REGISTER_PHONE_KEY + session.getId());
            if(StringUtil.isBlank(phoneCode)){
                header.setResultCode(Register.REGISTER_SSM_OVERTIME_ERROR);
                header.setResultMessage("验证码已失效");
                 responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS,
                        "验证码已失效", null);
                 responseData.setResponseHeader(header);
                 return responseData;
            }
          // 校验短信验证码
            if (!request.getPhoneVerifyCode().equals(phoneCode)) {
                header.setResultCode(Register.REGISTER_SSM_ERROR);
                header.setResultMessage("短信验证码错误");
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS,
                        "短信验证码错误", null);
                responseData.setResponseHeader(header);
                return responseData;
            }
            IRegisterSV iRegisterSV = DubboConsumerFactory.getService("iRegisterSV");
             PhoneRegisterResponse response = iRegisterSV.registerByPhone(request);
            String code = response.getResponseHeader().getResultCode();
            String accountId = Long.toString(response.getAccountId());
            String message = response.getResponseHeader().getResultMessage();
            if(Register.PHONE_NOTONE_ERROR.equals(code)){
                header.setResultCode(Register.PHONE_NOTONE_ERROR);
                header.setResultMessage("手机已经注册");
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "手机已经注册",
                        accountId);
                responseData.setResponseHeader(header);
                return responseData;
            }else if (ResultCode.SUCCESS_CODE.equals(code)) {
                header.setResultCode(Register.REGISTER_SUCCESS_ID);
                header.setResultMessage("注册成功");
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "注册成功",
                        accountId);
                responseData.setResponseHeader(header);
            } else {
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, message,
                        null);
            }
        } catch (Exception e) {
            LOG.error("注册失败！", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "注册失败",
                    null);
        }

        return responseData;
    }

    /**
     * 绑定email
     * 
     * @param request
     * @return
     */
    @RequestMapping("/bindEmail")
    @ResponseBody
    public ResponseData<String> bindEmail(UpdateEmailReq request, HttpSession session) {
        ResponseData<String> responseData = null;
       
        try {
            // 校验验证码是否正确
            String inputIdentify = request.getIdentifyCode();
            // 获取缓存中的验证码
            ICacheClient iCacheClient = CacheClientFactory
                    .getCacheClient(Register.CACHE_NAMESPACE);
            String identifyCode = iCacheClient
                    .get(Constants.Register.REGISTER_EMAIL_KEY + session.getId());
            ResponseHeader header = new ResponseHeader();
            header.setIsSuccess(true);
            //校验邮箱验证码是否失效
            if(StringUtil.isBlank(identifyCode)){
                header.setResultCode(Register.REGISTER_EMAIL_OVERTIME_ERROR);
                header.setResultMessage("邮箱验证码失效");
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS,
                        "邮箱验证码失效", null);
                responseData.setResponseHeader(header);
                return responseData;
            }
            //校验邮箱验证码是否正确
            if (!inputIdentify.equals(identifyCode)) {
                header.setResultCode(Register.REGISTER_EMAIL_ERROR);
                header.setResultMessage("邮箱验证码错误");
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS,
                        "验证码不正确", null);
                responseData.setResponseHeader(header);
                return responseData;
            }
            IAccountSecurityManageSV iAccountSecurityManageSV = DubboConsumerFactory
                    .getService("iAccountSecurityManageSV");
            AccountEmailRequest req = new AccountEmailRequest();
            long accountId = Long.parseLong(request.getAccountId());
            req.setAccountId(accountId);
            req.setEmail(request.getEmail());
            req.setUpdateAccountId(accountId);
            BaseResponse  baseInfo = iAccountSecurityManageSV.setEmailData(req);
            String resultCode = baseInfo.getResponseHeader().getResultCode();
            String resultMessage = baseInfo.getResponseHeader().getResultMessage();
            if(Register.EMAIL_NOTONE_ERROR.equals(resultCode)){
                header.setResultCode(Register.EMAIL_NOTONE_ERROR);
                header.setResultMessage("邮箱已经注册");
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "邮箱已经注册",
                        null);
                responseData.setResponseHeader(header);
                return responseData;
            }else if (ResultCode.SUCCESS_CODE.equals(resultCode)) {
                header.setResultCode(Register.BAND_EMAIL_SUCCESS_ID);
                header.setResultMessage("绑定邮箱成功");
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "绑定邮箱成功",
                        null);
                responseData.setResponseHeader(header);
            } else {
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, resultMessage,
                        null);
            }
        } catch (Exception e) {
            LOG.error("绑定邮箱失败！", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "绑定邮箱失败",
                    null);
        }
       
        return responseData;
    }

    /**
     * 发送email
     * 
     * @param request
     * @return
     */
    @RequestMapping("/toSendEmail")
    @ResponseBody
    public ResponseData<String> sendEmail(UpdateEmailReq emailReq, HttpServletRequest request) {
        ResponseData<String> responseData = null;

        IAccountManageSV  iAccountManageSV=  DubboConsumerFactory
		        .getService("iAccountManageSV");
		AccountQueryRequest req = new AccountQueryRequest();
		String email = emailReq.getEmail();
		req.setAccountId(Long.valueOf(emailReq.getAccountId()));
		AccountQueryResponse response =  iAccountManageSV.queryBaseInfo(req);
		String nickName = Constants.Register.REGISTER_EMAIL_NICK+response.getNickName();
		String identifyCode = RandomUtil.randomNum(EmailVerifyConstants.VERIFY_SIZE);
		String[] tomails = new String[] { email };
        //超时时间
		String overTime = ObjectUtils.toString(EmailVerifyConstants.VERIFY_OVERTIME/60);
		String[] data = new String[] { nickName, identifyCode ,overTime};
		SendEmailRequest emailRequest = new SendEmailRequest();
		emailRequest.setSubject(EmailVerifyConstants.EMAIL_SUBJECT);
		emailRequest.setTemplateRUL(Register.TEMPLATE_EMAIL_URL);
		emailRequest.setTomails(tomails);
		emailRequest.setData(data);
		VerifyUtil.sendEmail(emailRequest);
		//存验证码到缓存
		String key = Register.REGISTER_EMAIL_KEY+request.getSession().getId();
		ICacheClient  iCacheClient=  CacheClientFactory.getCacheClient(Register.CACHE_NAMESPACE);
		iCacheClient.setex(key, EmailVerifyConstants.VERIFY_OVERTIME, identifyCode);
		responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "验证码获取成功",
		        key);
        return responseData;
    }

    /**
     * 获取验证码
     * 
     * @param request
     * @return
     */
    @RequestMapping("/getImageVerifyCode")
    public void getImageVerifyCode(HttpServletRequest request, HttpServletResponse response) {
    	String cacheKey =  Register.CACHE_KEY_VERIFY_PICTURE+request.getSession().getId();
        BufferedImage image = VerifyUtil.getImageVerifyCode(request, Register.CACHE_NAMESPACE,cacheKey);
        try {
            ImageIO.write(image, "PNG", response.getOutputStream());
        } catch (IOException e) {
            LOG.error("生成图片验证码错误：" + e);
            e.printStackTrace();
        }
    }

    /**
     * 发送短信
     * 
     * @param request
     * @return
     * @throws Exception
     * @throws CallerException
     */
    @RequestMapping("/toSendPhone")
    @ResponseBody
    public ResponseData<String> sendPhone(GetSMDataReq sMDataReq, HttpServletRequest request)
            throws CallerException, Exception {
        ResponseData<String> responseData = null;
        try {
            SMDataInfoNotify smData = new SMDataInfoNotify();
            smData.setTenantId(request.getSession().getId());
            smData.setSystemId(Constants.SYSTEM_ID);
            smData.setMsgSeq(VerifyUtil.createPhoneMsgSeq());
            List<SMData> dataList = new ArrayList<SMData>();
            SMData data = new SMData();
            data.setPhone(sMDataReq.getPhone());
            data.setServiceType(PhoneVerifyConstants.SERVICE_TYPE);
            data.setTemplateId(PhoneVerifyConstants.TEMPLATE_REGISTER_ID);
            String identifyCode = RandomUtil.randomNum(PhoneVerifyConstants.VERIFY_SIZE);
            String codeContent = "${VERIFY}:" + identifyCode;
            String timeContent = "^${VALIDMINS}:" + PhoneVerifyConstants.VERIFY_OVERTIME;
            data.setGsmContent(codeContent + timeContent);
            dataList.add(data);
            smData.setDataList(dataList);
            SMSServices sMSServices = DubboConsumerFactory.getService("sMSServices");
            sMSServices.dataInput(smData);
            // 存验证码到缓存
            String key = Register.REGISTER_PHONE_KEY + request.getSession().getId();
            ICacheClient iCacheClient = CacheClientFactory.getCacheClient(Register.CACHE_NAMESPACE);
            iCacheClient.setex(key, PhoneVerifyConstants.VERIFY_OVERTIME, identifyCode);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "验证码获取成功",
                    key);
        } catch (Exception e) {
            LOG.error("验证码获取失败！", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "验证码获取失败",
                    null);
        }
        return responseData;
    }
}
