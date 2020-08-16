package com.gao.blog.service;

import com.gao.blog.dao.FollowsRepository;
import com.gao.blog.pojo.Follow;
import com.gao.blog.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class HomeServiceImpl implements HomeService{
    @Autowired
    FollowsRepository followsRepository;

    /**
     * 我的关注数据查询
     * @param page
     * @param model
     */
    @Override
    public void follows(Integer page, Model model) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Pageable pageable = PageRequest.of(page-1,20);
        Page<Follow> follows = followsRepository.findFollows(user.getId(), pageable);
        model.addAttribute("follows",follows);
    }
}
