package com.gao.blog.web.test;

import com.gao.blog.dao.CommentRepository;
import com.gao.blog.dao.FavorReponsitory;
import com.gao.blog.dao.NotifyRepository;
import com.gao.blog.dao.UserRepository;
import com.gao.blog.pojo.*;
import com.gao.blog.service.BlogService;
import com.gao.blog.service.TagService;
import com.gao.blog.service.TypeService;
import com.gao.blog.service.UserService;
import com.gao.blog.util.DigestHelper;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
    @Autowired
    UserService userService;


    /**
     * 后台主页
     *
     * @param model
     * @return
     */
    @GetMapping("/indexs")
    public String index(Model model) {
        return "admins/index";
    }

    /**
     * 后台登录页面
     * @return
     */
    @GetMapping({"/login", "", "/"})
    public String login() {
        return "admins/login";
    }

    /**
     * 后台ajax异步登录
     * @param username
     * @param password
     * @param session
     * @param attributes
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public Result adminlogin(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes attributes) {
        User username1 = userRepository.findByUsername(username);
        if (username1 != null) {
            String s = username1.getSalt();
            String newpassword = DigestHelper.getSha1(password + s);
            User user = userService.checkUser(username, newpassword);
            if (user != null) {
                if (user.getStatus()) {
                    if (user.getAuthority().equals("管理员")) {
                        session.setAttribute("superUser", user);
                        return Result.of(200);
                    } else {
                        attributes.addFlashAttribute("message", "访问权限不足");
                        return Result.of(300, "权限不足");
                    }
                } else {
                    attributes.addFlashAttribute("message", "状态异常，请联系管理员");
                    return Result.of(500, "状态异常，请联系管理员");
                }
            } else {
                attributes.addFlashAttribute("message", "密码错误");
                return Result.of(301, "密码错误");
            }
        } else {
            attributes.addFlashAttribute("message", "用户名错误");
            return Result.of(302, "用户名错误");
        }
    }
    /**
     * 后台用户列表展示
     * @param model
     * @param pageable
     * @return
     */
    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 10,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable){
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
    public String commentList(Model model, @PageableDefault(size = 10,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable){
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
    public String typesList(Model model, @PageableDefault(size = 10,direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("page",typeService.listType(pageable));
        return "admins/types-list";
    }

//    @GetMapping("/tagsList")
//    public String tags(@PageableDefault(size = 2,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable, Model model) {
//        model.addAttribute("page", tagService.listTag(pageable));
//        return "admins/tags-list";
//    }
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

    /**
     * 处理添加类型的处理
     * @param name
     * @return
     */
    @ResponseBody
    @PostMapping("/addTypes")
    public Result post(@RequestParam String name){

        Type type1 = typeService.getTypeByName(name);
        if (type1 != null){
            return Result.of(500,"不能重复添加");
        }
        Type type = new Type();
        type.setName(name);
        Type t = typeService.saveType(type);
        if (t == null){
            return Result.of(201,"新增失败");
        }else {
            return Result.of(200,"新增成功");
        }
    }

    /**
     * 类型修改页面跳转和传数据
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/types/{id}/input")
    public String typeUpdate(@PathVariable("id") Long id, Model model){
        model.addAttribute("ty",typeService.getType(id));
        return "admins/type-update";
    }

    /**
     * 修改页面的提交
     * @param type
     * @param id
     * @return
     */
    @Transactional
    @ResponseBody
    @PostMapping("/types/{id}")
    public Result editPost(@Valid Type type,@PathVariable("id") Long id){

        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null){
           return Result.of(500,"不能添加重复分类");
        }
        Type t = typeService.updateType(id,type);
        if (t == null){
           return Result.of(404,"更新失败");
        }else {
            return Result.of(200,"更新成功");
        }
    }
}
