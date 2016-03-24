package com.ai.opt.uac.web.controller.accountcenter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/updatePassword")
@Controller
public class UpdatePasswordController {
    @RequestMapping("/updatePasswordStart")
    public ModelAndView updatePasswordStart(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/update-password-start");
    }

    @RequestMapping("/updatePasswordNew")
    public ModelAndView updatePasswordNew(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/update-password-new");
    }

    @RequestMapping("/updatePasswordSuccess")
    public ModelAndView updatePasswordSuccess(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/update-password-success");
    }
}
