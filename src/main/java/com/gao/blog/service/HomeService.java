package com.gao.blog.service;

import com.gao.blog.pojo.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

public interface HomeService {

    /**
     * 我的关注数据获取
     * @param page
     */
    Page<Follow> follows(Pageable page);

    /**
     * 我的粉丝数据获取
     * @param page
     */
    Page<Follow> fans(Pageable page);
}
