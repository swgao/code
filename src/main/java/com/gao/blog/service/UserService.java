package com.gao.blog.service;

import com.gao.blog.pojo.User;
import com.gao.blog.vo.Result;

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

    Result updateProfile(String nickname, String sign);
}
