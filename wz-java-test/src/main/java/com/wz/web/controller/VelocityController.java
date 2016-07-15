package com.wz.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wangzhen on 2016-07-12.
 */
@RequestMapping(value="/vm")
@Controller
public class VelocityController {

        @RequestMapping(value="/index")
        public String index(Model model) {
            String name = "tester";
            model.addAttribute("name", name);
            return "index";
        }
}
