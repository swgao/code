package com.gao.blog.dao;

import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
   /**
    * 根据id并且是第一层评论
    * @param blogId
    * @param sort
    * @return
    */
   List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

   /**
    * 根据博客id查询博客的全部评论
    * @param blogId
    * @return
    */
   List<Comment> findByBlogId(Long blogId);

   /**
    * 根据博客id查询评论数量
    * @param blogId
    * @return
    */
   @Query("select count(*) from Comment c where c.blog.id=?1")
   int countByBlogId(Long blogId);

   /**
    * 查询所有评论
    * @param pageable
    * @return
    */
   Page<Comment> findAll(Pageable pageable);
}
