package com.gao.blog.web.test;

import com.gao.blog.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexsController {

    @Autowired
    UserRepository userRepository;
    @GetMapping("/indexs")
    public String index(Model model){
        model.addAttribute("users",userRepository.findAll());
        return "admins/index";
    }
    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("users",userRepository.findAll());
        return "admins/member-list";
    }
}
