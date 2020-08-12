package com.gao.blog.web;

import com.gao.blog.dao.UserRepository;
import com.gao.blog.pojo.User;
import com.gao.blog.util.DigestHelper;
import com.gao.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    public Map<String, Object> register(@RequestBody(required = true)User user) throws InterruptedException {
        Thread.sleep(3000);
        User username = userRepository.findByUsername(user.getUsername());
        User email = userRepository.findByEmail(user.getEmail());
        User phone = userRepository.findByPhone(user.getPhone());
        if (username != null){
            return Result.of(300,"用户名存在");
        }
        if (email != null){
            return Result.of(301,"邮箱已经存在");
        }
        if (phone != null){
            return Result.of(302,"手机号已经存在");
        }
//        DigestUtils.md5DigestAsHex();
        user.setSalt(UUID.randomUUID().toString());
        String m = user.getPassword();
        String s = user.getSalt();
        String newpassword = DigestHelper.getSha1(m+s);
        user.setPassword(newpassword);

        DigestHelper.getSha1("123456");
        user.setCreateTime(new Date());
        user.setAvatar("");
        user.setType(1);
        user.setUpdateTime(new Date());
        userRepository.save(user);
        return Result.of(200,"注册成功");
    }

}
