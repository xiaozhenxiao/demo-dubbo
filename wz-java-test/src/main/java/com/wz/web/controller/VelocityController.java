package com.wz.web.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wangzhen on 2016-07-12.
 */
@RequestMapping(value="/vm")
@Controller
@Scope("prototype")//默认controller是单例的
public class VelocityController {
    private int index = 0;

        @RequestMapping(value="/index")
        public String index(Model model) {
            String name = "tester";
            model.addAttribute("name", name);
            model.addAttribute("index", ++index);
            return "index";
        }
}
