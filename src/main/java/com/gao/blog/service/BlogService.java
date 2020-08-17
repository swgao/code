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

    /**
     * 根据typeId查询分页数据
     * @param typesId
     * @param pageable
     * @return
     */
    Page<Blog> listBlog(Long typesId,Pageable pageable);

    /**
     * 根据typeId的数据进行分页跳转
     * @param pageable
     * @return
     */
    Page<Blog> listBlogTypeId(Pageable pageable);

    /**
     * 根据tagId查询分页数据
     * @param pageable
     * @param tagId
     * @return
     */
    Page<Blog> listBlog(Pageable pageable,Long tagId);

    /**
     * 根据tagId的数据进行分页跳转
     * @param pageable
     * @return
     */
    Page<Blog> listBlogTagId(Pageable pageable);

    /**
     * 主页的blog分页
     * @param pageable
     * @return
     */
    Page<Blog> listBlog(Pageable pageable);
    Page<Blog> listBlog(Pageable pageable,String query);

    /**
     * 根据userId查询blog
     * @param pageable
     * @param userId
     * @return
     */
    Page<Blog> homeBlog(Pageable pageable,Long userId);
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

    /**
     * blog总数
     * @return
     */
    Long countBlog();


}
