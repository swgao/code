package com.gao.blog.web.home;

import com.gao.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 处理关注
 */
@Controller
public class ProfileController {

    @Autowired
    UserService userService;
    /**
     * 关注某个人，要求已登录
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/follow")
    public Object follow(long id){
        return userService.saveFollow(id);
    }

    /**
     * 检查是否关注了某个人，要求已登录
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping("/follow/check/{userId}")
    public Object followCheck(@PathVariable long userId){
        return userService.followCheck(userId);
    }

    /**
     * 取消关注某个人，要求已登录
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/unfollow")
    public Object unfollow(long id){
        return userService.unFollow(id);
    }
}
