package com.gao.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping({"/home/index","/home"})
    public String index(){
        return "home/index";
    }
    @RequestMapping("/2")
    public String avatar(){
        return "home/avatar";
    }
    @RequestMapping("/profile")
    public String profile(){
        return "home/profile";
    }
    @RequestMapping("/password")
    public String password(){
        return "home/password";
    }
}
