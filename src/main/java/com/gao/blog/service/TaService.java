package com.gao.blog.service;

import com.gao.blog.vo.Result;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

public interface TaService {
    /**
     * 查询user的基本信息，user的文章分页
     * @param userId
     * @param page
     * @return  {user.Page<Blog>}
     */
    void findData(long userId, int page, Model mav);
}
