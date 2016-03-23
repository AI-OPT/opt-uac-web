package com.ai.opt.uac.web.controller.demo;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;



@RequestMapping("/ac")
@Controller
public class ACController {

    private static final Logger LOG = LoggerFactory.getLogger(ACController.class);

    @RequestMapping("/commlabel")
    public ModelAndView index(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("demo/commlabel");
        return view;
    }
    
    @RequestMapping("/parent")
    public ModelAndView parent(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("demo/parent");
        return view;
    }
    
    @RequestMapping("/paging")
    @ResponseBody
    public JSONObject paging(HttpServletRequest request) {
        
        String str = "{'view':[{'name':'nimo','age':23},{'name':'potato','age':18},{'name':'little nimo','age':3}],'pagecount':10}";
        JSONObject data = JSONObject.fromObject(str);
        return data;
    }


}
