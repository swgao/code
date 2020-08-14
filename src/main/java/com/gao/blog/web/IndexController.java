package com.gao.blog.web;

import com.gao.blog.NotFoundException;
import com.gao.blog.pojo.User;
import com.gao.blog.service.BlogService;
import com.gao.blog.service.TagService;
import com.gao.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
//    @RequestMapping({"/index","/{id}/{name}"})
//    public String index(@PathVariable("id") int id,@PathVariable("name") String name){
////        int i = 1/0;
////        String blog = null;
////        if (blog == null){
////            throw new NotFoundException("博客不存在");
////        }
//        System.out.println("--------index------");
//        return "index";
//    }

    @Autowired
    BlogService blogService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;
    @RequestMapping({"/index","/"})
    public String index(Model model, @PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types",typeService.listTypeTop(3));
        model.addAttribute("tags",tagService.listTagTop(10));
        model.addAttribute("recommendBLogs",blogService.listRecommendBlogTop(8));
        return "index";
    }
    @RequestMapping("/blog/{id}")
    public String blog(@PathVariable("id") Long id,Model model){
        model.addAttribute("blog",blogService.getBlog(id));
        return "blog";
    }
    @GetMapping("/tags")
    public String tags(){
        return "tags";
    }
    @PostMapping("/search")
    public String search(@RequestParam String query, Model model, @PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("page",blogService.listBlog(pageable,"%"+query+"%"));
        model.addAttribute("query",query);
        return "search";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        model.addAttribute("newblogList",blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }
}
