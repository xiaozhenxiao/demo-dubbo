package com.wz.web.controller;

import com.wz.web.domain.User;
import com.wz.web.service.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;

/**
 * Created by wangzhen on 2016-07-12.
 */
@RequestMapping(value = "/vm")
@Controller
//@Scope("prototype")//默认controller是单例的
public class VelocityController {
    private int index = 0;
    private Random random = new Random();

    @Resource
    private UserService userService;

    @RequestMapping(value = "/index")
    public String index(Model model) {
        String name = "tester";
        model.addAttribute("name", name);
        model.addAttribute("index", ++index);
        return "index";
    }

    @RequestMapping("/body")
    public ModelAndView handle(@RequestBody String requestBody) throws UnsupportedEncodingException {
        ModelAndView mv = new ModelAndView();
        User user = new User();
        user.setUsername("wang" + random.nextInt());
        user.setPassword("123456" + random.nextInt());
        userService.addUser(user);
        mv.addObject("name", user.getUsername());
        mv.addObject("index", ++index);
        mv.setViewName("index");
        System.out.println("----------------------------------------------------------------");
        System.out.println(URLDecoder.decode(requestBody, "UTF-8"));
        System.out.println("----------------------------------------------------------------");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/image")
    public byte[] handle2() throws IOException {
        org.springframework.core.io.Resource res = new ClassPathResource("image.jpg");
        byte[] fileData = FileCopyUtils.copyToByteArray(res.getInputStream());
        return fileData;
    }
}
