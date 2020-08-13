package com.gao.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @RequestMapping({"/index","","/"})
    public String index(){
        return "home/index";
    }

    /**
     * 头像修改页面跳转
     * @return
     */
    @RequestMapping("/avatar")
    public String avatar(){
        return "home/avatar";
    }

    /**
     * 基本信息修改页面跳转
     * @return
     */
    @RequestMapping("/profile")
    public String profile(){
        return "home/profile";
    }

    /**
     * 密码修改页面跳转
     * @return
     */
    @RequestMapping("/password")
    public String password(){
        return "home/password";
    }
}
