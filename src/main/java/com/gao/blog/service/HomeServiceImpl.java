package com.gao.blog.service;

import com.gao.blog.dao.FollowsRepository;
import com.gao.blog.dao.NotifyRepository;
import com.gao.blog.pojo.Follow;
import com.gao.blog.pojo.Notify;
import com.gao.blog.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class HomeServiceImpl implements HomeService{
    @Autowired
    FollowsRepository followsRepository;
    @Autowired
    NotifyRepository notifyRepository;

    /**
     * 我的关注数据查询
     * @param page
     */
    @Override
    public Page<Follow> follows(Pageable page) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Page<Follow> follows = followsRepository.findFollows(user.getId(), page);
        return follows;
    }

    /**
     * 我的粉丝 数据查询
     * @param page
     * @return
     */
    @Override
    public Page<Follow> fans(Pageable page) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Page<Follow> fans = followsRepository.findFans(user.getId(), page);
        return fans;

    }

    /**
     * 通知业务查询
     * @param pageable
     * @return
     */
    @Override
    public Page<Notify> notifies(Pageable pageable) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        return notifyRepository.findByUser(user.getId(),pageable);
    }
}
