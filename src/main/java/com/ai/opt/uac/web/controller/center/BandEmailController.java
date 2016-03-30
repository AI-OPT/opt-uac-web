package com.ai.opt.uac.web.controller.center;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/center/bandEmail")
@Controller
public class BandEmailController {
    @RequestMapping("/bandEmailStart")
    public ModelAndView bandEmailStart(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/band-email-start");
    }

    @RequestMapping("/bandEmailNew")
    public ModelAndView bandEmailNew(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/band-email-new");
    }

    @RequestMapping("/bandEmailSuccess")
    public ModelAndView bandEmailSuccess(HttpServletRequest request) {

        return new ModelAndView("jsp/accountcenter/band-email-success");
    }

}
