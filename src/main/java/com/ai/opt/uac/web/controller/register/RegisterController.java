package com.ai.opt.uac.web.controller.register;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ai.opt.base.exception.RPCSystemException;
import com.ai.opt.base.vo.BaseResponse;
import com.ai.opt.sdk.util.DubboConsumerFactory;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.uac.api.register.interfaces.IRegisterSV;
import com.ai.opt.uac.api.register.param.PhoneRegisterRequest;
import com.ai.opt.uac.api.register.param.PhoneRegisterResponse;
import com.ai.opt.uac.api.security.interfaces.IAccountSecurityManageSV;
import com.ai.opt.uac.api.security.param.AccountEmailRequest;
import com.ai.opt.uac.web.model.register.UpdateEmailReq;
import com.ai.opt.uac.web.util.Md5Util;

@RequestMapping("/reg")
@Controller
public class RegisterController {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

    @RequestMapping("/toRegister")
    public ModelAndView register(HttpServletRequest request) {

        return new ModelAndView("jsp/register/register");
    }

    @RequestMapping("/toRegisterEmail")
    public ModelAndView registerEmail(@RequestParam(value = "accountId", required = false) String accountId,HttpServletRequest request) {
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
            IRegisterSV iRegisterSV = DubboConsumerFactory.getService("iRegisterSV");
            PhoneRegisterResponse response = iRegisterSV.registerByPhone(request);
            String code = response.getResponseHeader().getResultCode();
            String accountId = Long.toString(response.getAccountId());
            String message = response.getResponseHeader().getResultMessage();
            if (code.equals("000000")) {
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "注册成功",
                        accountId);
            } else {
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, message, null);
            }
        } catch (RPCSystemException e) {
            LOG.info("添加失败", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "注册失败", null);
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
            IAccountSecurityManageSV iAccountSecurityManageSV = DubboConsumerFactory
                    .getService("iAccountSecurityManageSV");
            AccountEmailRequest req = new AccountEmailRequest();
            long accountId = Long.parseLong(request.getAccountId());
            req.setAccountId(accountId);
            req.setEmail(request.getEmail());
            req.setUpdateAccountId(accountId);
            BaseResponse baseInfo =  iAccountSecurityManageSV.setEmailData(req);
            String errorCode = baseInfo.getResponseHeader().getResultCode();
            String resultMessage = baseInfo.getResponseHeader().getResultMessage();
            if(errorCode.equals("000000")){
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "信息修改成功",
                        null);
            }else{
                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, resultMessage, null); 
            }
           
        } catch (RPCSystemException e) {
            LOG.error("查询失败！", e);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "信息修改失败",
                    null);
            e.printStackTrace();
        }
        return responseData;
    }

}
