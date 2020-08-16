package com.gao.blog.service;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

public interface HomeService {

    /**
     * 我的关注数据获取
     * @param page
     * @param model
     */
    void follows(Integer page, Model model);
}
