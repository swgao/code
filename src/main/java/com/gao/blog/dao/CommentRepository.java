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

   List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}
