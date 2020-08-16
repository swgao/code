package com.gao.blog.service;

import com.gao.blog.pojo.User;
import com.gao.blog.vo.Result;

import javax.servlet.http.HttpSession;

public interface UserService {
    /**
     * 检查用户
     * @param username
     * @param password
     * @return
     */
    User checkUser(String username,String password);

    /**
     * 修改密码
     * @param oldPassword
     * @param password
     * @return
     */
    Result updatePasswrod(String oldPassword,String password);

    /**
     * 基本信息页面修改
     * @param nickname
     * @param sign
     * @return
     */
    Result updateProfile(String nickname, String sign);

    /**
     * 喜欢
     * @param blogId
     * @param session
     * @return
     */
    String saveFavor(long blogId, HttpSession session);

    /**
     * 点击关注处理
     * @param userId
     * @return
     */
    Result saveFollow(long userId);

    /**
     * 当前登录用户检查是否关注了某个人
     * @param userId
     * @return
     */
    Result followCheck(long userId);
}
