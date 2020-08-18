package com.gao.blog.web;

import com.gao.blog.NotFoundException;
import com.gao.blog.dao.CommentRepository;
import com.gao.blog.dao.FavorReponsitory;
import com.gao.blog.dao.UserRepository;
import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.User;
import com.gao.blog.service.BlogService;
import com.gao.blog.service.TagService;
import com.gao.blog.service.TypeService;
import com.gao.blog.service.UserService;
import com.gao.blog.vo.BlogVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    BlogService blogService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;
    @Autowired
    FavorReponsitory favorReponsitory;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    /**
     * 主页面
     * @param model
     * @param pageable
     * @return
     */
    @RequestMapping({"/index","/"})
    public String index(Model model, @PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types",typeService.listTypeTop(3));
        model.addAttribute("tags",tagService.listTagTop(10));
        model.addAttribute("recommendBLogs",blogService.listRecommendBlogTop(8));
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
            System.out.println(count);
            // 查询喜欢的个数
            int favorCount = favorReponsitory.countByBlog(a.getId());
            v.setXihuan(favorCount);
            // 然后把a拷贝到v
            BeanUtils.copyProperties(a,v);
            //添加到列表
            lists.add(v);

        }
        model.addAttribute("pages",lists);
        return "index";
    }

    /**
     * 根据id 进入博客详情
     * @param id
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/blog/{id}")
    public String blog(@PathVariable("id") Long id,Model model,HttpSession session){
        // 返回给请求博客页面的评论个数
        model.addAttribute("list",commentRepository.findByBlogId(id));
        System.out.println(commentRepository.findByBlogId(id));
        User user = (User) session.getAttribute("user");
        if (user != null){
            model.addAttribute("ids",1);
            // 如果有用户，就查出用户是否喜欢该帖子
            User user1 = userRepository.getOne(user.getId());
            if (favorReponsitory.find(user1.getId(),id)==null){
                model.addAttribute("favor_null",1);
            }else{
                model.addAttribute("favor_null",2);
                model.addAttribute("favor",favorReponsitory.find(user1.getId(),id));
            }
        }else{
            model.addAttribute("ids",2);
        }
        model.addAttribute("blog",blogService.getBlog(id));
        model.addAttribute("xihuan",favorReponsitory.countByBlog(id));
        return "blog";
    }

    /**
     * 标签页
     * @return
     */
    @GetMapping("/tags")
    public String tags(){
        return "tags";
    }

    /**
     * 搜索处理
     * @param query
     * @param model
     * @param pageable
     * @return
     */
    @PostMapping("/search")
    public String search(@RequestParam String query, Model model, @PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("page",blogService.listBlog(pageable,"%"+query+"%"));
        model.addAttribute("query",query);
        return "search";
    }

    /**
     * 最下面的最新博客处理
     * @param model
     * @return
     */
    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        model.addAttribute("newblogList",blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

}
