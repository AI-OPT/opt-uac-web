package com.ai.opt.uac.web.controller.retakepassword;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/retakePassword")
@Controller
public class RetakePasswordController {
    @RequestMapping("/confirmByEmail")
    public ModelAndView retakePassEmail(HttpServletRequest request) {
        return new ModelAndView("jsp/retakepassword/retake-password-email");
    }

    @RequestMapping("/confirmByPhone")
    public ModelAndView retakePassPhone(HttpServletRequest request) {
        return new ModelAndView("jsp/retakepassword/retake-password-phone");
    }

    @RequestMapping("/setNewPwd")
    public ModelAndView retakePassNew(HttpServletRequest request) {

        return new ModelAndView("jsp/retakepassword/retake-password-new");
    }

    @RequestMapping("/success")
    public ModelAndView retakePassSuccess(HttpServletRequest request) {

        return new ModelAndView("jsp/retakepassword/retake-password-success");
    }

}
