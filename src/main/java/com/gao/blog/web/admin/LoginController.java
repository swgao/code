package com.gao.blog.web.admin;

import com.gao.blog.dao.UserRepository;
import com.gao.blog.pojo.User;
import com.gao.blog.service.UserService;
import com.gao.blog.util.DigestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 后台用户Controller
 */
@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * 后台登录页面跳转
     * @return
     */
    @GetMapping
    public String LoginPage(){
        return "admin/login";
    }

    /**
     * 后台登录判断
     * @param username  用户名
     * @param password  密码
     * @param session   session
     * @param attributes    信息携带
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes attributes){
        User username1 = userRepository.findByUsername(username);
        if (username1 != null){
            String s = username1.getSalt();
            String newpassword = DigestHelper.getSha1(password+s);
            User user = userService.checkUser(username,newpassword);
            if (user!=null){
                user.setPassword(null);
                session.setAttribute("user",user);
                return "admin/index";
            }else {
                attributes.addFlashAttribute("message","密码错误");
                return "redirect:/admin";
            }
        }else {
            attributes.addFlashAttribute("message","用户名错误");
            return "redirect:/admin";
        }
    }

    /**
     * 后台注销
     * @param session session
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
