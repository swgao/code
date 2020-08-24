package com.gao.blog.web.test;

import com.gao.blog.dao.CommentRepository;
import com.gao.blog.dao.FavorReponsitory;
import com.gao.blog.dao.NotifyRepository;
import com.gao.blog.dao.UserRepository;
import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.Comment;
import com.gao.blog.pojo.Notify;
import com.gao.blog.pojo.User;
import com.gao.blog.service.BlogService;
import com.gao.blog.service.TagService;
import com.gao.blog.service.TypeService;
import com.gao.blog.vo.BlogVO;
import com.gao.blog.vo.Result;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/admins")
@Controller
public class IndexsController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BlogService blogService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    FavorReponsitory favorReponsitory;
    @Autowired
    NotifyRepository notifyRepository;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;

    /**
     * 后台主页
     * @param model
     * @return
     */
    @GetMapping("/indexs")
    public String index(Model model){
        return "admins/index";
    }

    /**
     * 后台用户列表展示
     * @param model
     * @param pageable
     * @return
     */
    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 7,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("users",userRepository.findAll(pageable));
        return "admins/member-list";
    }

    /**
     * 后台博客列表展示
     * @param model
     * @param pageable
     * @return
     */
    @GetMapping("/blogsList")
    public String blogsList(Model model, @PageableDefault(size = 10,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("page",blogService.listBlog(pageable));
        // 拿到博客
        Page<Blog> blogs = blogService.listBlog(pageable);
        List<Blog> lists =new ArrayList();
        // 遍历
        for (int i = 0;i < blogs.getContent().size();i++) {
            // 拿到循环的单个博客
            Blog a = blogs.getContent().get(i);
            // 根据单个博客id进行查找
            int count = commentRepository.countByBlogId(a.getId());
            // 新建BlogVO
            BlogVO v = new BlogVO();
            // 把评论数f赋值
            v.setPinglun(count);
            // 查询喜欢的个数
            int favorCount = favorReponsitory.countByBlog(a.getId());
            v.setXihuan(favorCount);
            // 然后把a拷贝到v
            BeanUtils.copyProperties(a,v);
            //添加到列表
            lists.add(v);

        }
        model.addAttribute("pages",lists);
        return "admins/order-list1";
    }

    /**
     * 后台评论展示
     * @param model
     * @param pageable
     * @return
     */
    @GetMapping("/commentList")
    public String commentList(Model model, @PageableDefault(size = 2,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("comments",commentRepository.findAll(pageable));
        return "admins/comment-list";
    }

    /**
     * 评论删除
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/comment/delete")
    public Result comment_delete(@RequestParam Long id){
        commentRepository.deleteById(id);
        return Result.of(200);
    }

    /**
     * 类型列表展示
     * @param model
     * @param pageable
     * @return
     */
    @GetMapping("/typesList")
    public String typesList(Model model, @PageableDefault(size = 2,direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("page",typeService.listType(pageable));
        return "admins/types-list";
    }

    /**
     * 后台修改类型
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable("id") Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }
    @GetMapping("/tagsList")
    public String tags(@PageableDefault(size = 2,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", tagService.listTag(pageable));
        return "admins/tags-list";
    }
    /**
     * 用户停用
     * @param id
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping("/status")
    public Result status(@RequestParam long id){
        User one = userRepository.getOne(id);
        one.setStatus(false);
        userRepository.save(one);
        return Result.of(200);
    }

    /**
     * 取消用户停用
     * @param id
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping("/unstatus")
    public Result unstatus(@RequestParam long id){
        User one = userRepository.getOne(id);
        one.setStatus(true);
        userRepository.save(one);
        return Result.of(200);
    }

    /**
     * 后台博客删除
     * @param id
     */
    @Transactional
    @ResponseBody
    @GetMapping("/blogs/delete")
    public void delete(@RequestParam("id") Long id){
        blogService.deleteBlog(id);
    }

    /**
     * 后台删除一个用户
     * @param id
     */
    @Transactional
    @ResponseBody
    @RequestMapping("/deleteUser")
    public void deleteUser(@RequestParam Long id){
        System.out.println(id);
        userRepository.deleteById(id);
        // 删除和他关联的所有通知
        notifyRepository.deleteUserId(id);
    }

}
