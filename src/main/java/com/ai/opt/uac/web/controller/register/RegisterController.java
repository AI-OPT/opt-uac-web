package com.ai.opt.uac.web.controller.register;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ai.opt.base.exception.RPCSystemException;
import com.ai.opt.sdk.web.model.ResponseData;
import com.ai.opt.uac.api.register.interfaces.IRegisterSV;
import com.ai.opt.uac.api.register.param.PhoneRegisterRequest;
import com.ai.opt.uac.api.register.param.PhoneRegisterResponse;
import com.ai.opt.uac.web.util.Md5Util;

@Controller
public class RegisterController {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    IRegisterSV iRegisterSV;

    @RequestMapping("/toRegister")
    public ModelAndView register(HttpServletRequest request) {

        return new ModelAndView("jsp/register/register");
    }

    @RequestMapping("/toRegisterEmail")
    public ModelAndView registerEmail(HttpServletRequest request) {

        return new ModelAndView("jsp/register/register-email");
    }

    @RequestMapping("/toRegisterSuccess")
    public ModelAndView registerSuccess(HttpServletRequest request) {

        return new ModelAndView("jsp/register/register-success");
    }

    @RequestMapping("/register")
    @ResponseBody
    public ResponseData<String> addAccount(PhoneRegisterRequest request, HttpSession session) {
        ResponseData<String> reaponseData = null;
        // MD5加密
        request.setAccountPassword(Md5Util.stringMD5(request.getAccountPassword()));

        try {
            PhoneRegisterResponse response = iRegisterSV.registerByPhone(request);
            String code = response.getResponseHeader().getResultCode();
            String message = response.getResponseHeader().getResultMessage();

            if (code.equals("000000")) {
                reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "注册成功",
                        null);
            } else {
                reaponseData = new ResponseData<String>(code, message, null);
            }
        } catch (RPCSystemException e) {
            LOG.info("添加失败", e);
            reaponseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "注册失败", null);

        }
        return reaponseData;
    }

}
