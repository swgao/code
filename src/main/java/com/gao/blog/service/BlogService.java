package com.gao.blog.service;

import com.gao.blog.pojo.Blog;
import com.gao.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface BlogService {
    /**
     * 根据id获取博客
     * @param id
     * @return
     */
    Blog getBlog(Long id);

    /**
     * 分页查询博客
     * @param pageable
     * @param blog
     * @return
     */
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable,Long tagId);
    Page<Blog> listBlog(Pageable pageable);
    Page<Blog> listBlog(Pageable pageable,String query);
    /**
     * 保存博客
     * @param blog
     * @return
     */
    Blog saveBlog(Blog blog);

    /**
     * 更新博客
     * @param id
     * @param blog
     * @return
     */
    Blog updateBlog(Long id,Blog blog);

    /**
     * 删除博客
     * @param id
     */
    void deleteBlog(Long id);


    List<Blog> listRecommendBlogTop(Integer size);

    Map<String,List<Blog>> archiveBlog();
    Long countBlog();


}
