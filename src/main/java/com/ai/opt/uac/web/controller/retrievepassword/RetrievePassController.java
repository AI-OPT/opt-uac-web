package com.ai.opt.uac.web.controller.retrievepassword;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/retrievePass")
@Controller
public class RetrievePassController {
    @RequestMapping("/retrievePassEmail")
    public ModelAndView retrievePassEmail(HttpServletRequest request) {

        return new ModelAndView("jsp/retrievepassword/retrieve-password-email");
    }

    @RequestMapping("/retrievePassPhone")
    public ModelAndView retrievePassPhone(HttpServletRequest request) {

        return new ModelAndView("jsp/retrievepassword/retrieve-password-phone");
    }

    @RequestMapping("/retrievePassNew")
    public ModelAndView retrievePassNew(HttpServletRequest request) {

        return new ModelAndView("jsp/retrievepassword/retrieve-password-new");
    }

    @RequestMapping("/retrievePassSuccess")
    public ModelAndView retrievePassSuccess(HttpServletRequest request) {

        return new ModelAndView("jsp/retrievepassword/retrieve-password-success");
    }

}
