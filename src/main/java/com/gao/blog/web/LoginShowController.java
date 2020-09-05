package com.gao.blog.web;

import com.gao.blog.dao.UserRepository;
import com.gao.blog.pojo.User;
import com.gao.blog.util.DigestHelper;
import com.gao.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Controller
public class LoginShowController {


    @Autowired
    UserRepository userRepository;

    @GetMapping("/re")
    public String toRegister(){
        return "register";
    }

    @RequestMapping("/register")
    public String register(User user, RedirectAttributes attributes, HttpServletRequest request, HttpSession session) throws InterruptedException {
        Thread.sleep(3000);
        User username = userRepository.findByUsername(user.getUsername());
        User email = userRepository.findByEmail(user.getEmail());
        User phone = userRepository.findByPhone(user.getPhone());
        String verifyCode = request.getParameter("verifyCode");
        if (username != null){
            attributes.addFlashAttribute("message","用户名已经存在");
            return "redirect:/re";
        }
        if (email != null){
            attributes.addFlashAttribute("message","邮箱已经存在");
            return "redirect:/re";
//            return Result.of(301,"邮箱已经存在");
        }
        if (phone != null){
            attributes.addFlashAttribute("message","手机号已经存在");
            return "redirect:/re";
//            return Result.of(302,"手机号已经存在");
        }
        if (!verifyCode.equals(session.getAttribute("verifyCode"))){
            attributes.addFlashAttribute("message","验证码不正确");
            return "redirect:/re";
        }
//        DigestUtils.md5DigestAsHex();
        user.setSalt(UUID.randomUUID().toString());
        String m = user.getPassword();
        String s = user.getSalt();
        String newpassword = DigestHelper.getSha1(m+s);
        user.setPassword(newpassword);
//        DigestHelper.getSha1("123456");
        user.setCreateTime(new Date());
//        user.setType(1);
        user.setStatus(true);
        user.setAuthority("用户");
        user.setUpdateTime(new Date());
        user.setAvatar("/images/logo.jpg");
        userRepository.save(user);
        return "admin/login";
//        return Result.of(200,"注册成功");
    }

}
