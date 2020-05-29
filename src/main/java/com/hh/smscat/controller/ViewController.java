package com.hh.smscat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

/**
 * Created by Owen Pan on 2016/10/8.
 */
@Controller
@RequestMapping("/view")
public class ViewController {

    @RequestMapping("/{folder}/{file}")
    public ModelAndView path(@PathVariable String folder, @PathVariable String file) {
        return new ModelAndView("/" + folder + "/" + file);
    }

    @RequestMapping("/{file}")
    public ModelAndView path(@PathVariable String file) {
        return new ModelAndView("/" + file);
    }

//    @RequestMapping(value="/**",method= RequestMethod.GET)
//    public String path(HttpServletRequest request) {
//        return request.getRequestURI().replace(request.getContextPath()+"/view/", "/");
//    }
}
