package com.gao.blog.web;

import com.gao.blog.dao.CommentRepository;
import com.gao.blog.dao.NotifyRepository;
import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.Comment;
import com.gao.blog.pojo.Notify;
import com.gao.blog.pojo.User;
import com.gao.blog.service.BlogService;
import com.gao.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    BlogService blogService;
    @Autowired
    NotifyRepository notifyRepository;

    @Value("${comment.avatar}")
    private String avatar;

    /**
     * 根据博客id返回对应的评论信息
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        // 返回给请求博客页面的评论个数
        model.addAttribute("list",commentRepository.findByBlogId(blogId));
        // 返回给指定区域处理好的评论信息
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    /**
     * 评论成功的处理
     * @param comment
     * @param session
     * @return
     */
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user = (User) session.getAttribute("user");
        comment.setAvatar(user.getAvatar());
        comment.setUser_id(user);
        Blog blog = blogService.getBlog(blogId);
        if ((blog.getUser().getId()).equals(user.getId())){
            comment.setAdminComment(true);
        }
        commentService.saveComment(comment);

        // 给文章作者发通知
        Notify notify = new Notify();
        notify.setAvatar(user.getAvatar());
        notify.setCreateTime(new Date());
        notify.setTitle(user.getNickname());
        notify.setUser(blog.getUser());
        notify.setUrl("/ta/"+user.getId());
        String s2 = String.format("评论了你的文章- <a href=\"/blog/%s\">点击查看详情</a>",blogId);
        notify.setContent(s2);
        notifyRepository.save(notify);
        return "redirect:/comments/"+blogId;
    }

}
