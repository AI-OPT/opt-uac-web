package com.ai.opt.uac.web.controller.register;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/register")
@Controller
public class RegisterController {
    @RequestMapping("/register")
    public ModelAndView register(HttpServletRequest request) {

        return new ModelAndView("jsp/register/register");
    }

    @RequestMapping("/registerEmail")
    public ModelAndView registerEmail(HttpServletRequest request) {

        return new ModelAndView("jsp/register/register-email");
    }

    @RequestMapping("/registerSuccess")
    public ModelAndView registerSuccess(HttpServletRequest request) {

        return new ModelAndView("jsp/register/register-success");
    }
}
