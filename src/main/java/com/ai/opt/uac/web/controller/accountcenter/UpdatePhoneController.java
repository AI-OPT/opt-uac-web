package com.ai.opt.uac.web.controller.accountcenter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/updatePhone")
@Controller
public class UpdatePhoneController {
    @RequestMapping("/updatePhoneStart")
    public ModelAndView updatePhoneStart(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/update-phone-start");
    }

    @RequestMapping("/updatePhoneNew")
    public ModelAndView updatePhoneNew(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/update-phone-new");
    }

    @RequestMapping("/updatePhoneSuccess")
    public ModelAndView updatePhoneSuccess(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/update-phone-success");
    }
}
