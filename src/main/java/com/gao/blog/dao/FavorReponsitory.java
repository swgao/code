package com.gao.blog.dao;

import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.Comment;
import com.gao.blog.pojo.Favor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavorReponsitory extends JpaRepository<Favor,Long> {
    /**
     * 根据userid和blogid查询用户是否喜欢该博客
     * @param user
     * @param blog
     * @return
     */
    @Query("from Favor f where f.user.id=?1 and f.blog.id=?2")
    Favor find(long user,long blog);

    /**
     * 根据blogid喜欢总数
     * @param blogId
     * @return
     */
    @Query("select count(id) from Favor f where f.blog.id=?1 and f.if_like=true ")
    int countByBlog(long blogId);

    /**
     * 根据userId查询用户喜欢的博客
     * @param userId
     * @return
     */
    @Query("from Favor f where f.user.id=?1 and f.if_like=true ")
    Page<Favor> findByBlog(long userId, Pageable page);

}
