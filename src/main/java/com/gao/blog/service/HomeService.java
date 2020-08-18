package com.gao.blog.service;

import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.Follow;
import com.gao.blog.pojo.Notify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    /**
     * 通知业务
     * @param pageable
     * @return
     */
    Page<Notify> notifies(Pageable pageable);

}
