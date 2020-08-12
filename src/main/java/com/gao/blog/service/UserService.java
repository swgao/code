package com.gao.blog.service;

import com.gao.blog.pojo.User;

public interface UserService {
    /**
     * 检查用户
     * @param username
     * @param password
     * @return
     */
    User checkUser(String username,String password);
}
