package com.ai.opt.uac.web.controller.accountcenter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/updateEmail")
@Controller
public class UpdateEmialController {
    @RequestMapping("/updateEmailStart")
    public ModelAndView updateEmailStart(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/update-email-start");
    }

    @RequestMapping("/updateEmailNew")
    public ModelAndView updateEmailNew(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/update-email-new");
    }

    @RequestMapping("/updateEmailSuccess")
    public ModelAndView updateEmailSuccess(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/update-email-success");
    }
}
