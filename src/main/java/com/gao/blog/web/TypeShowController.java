package com.gao.blog.web;

import com.gao.blog.dao.CommentRepository;
import com.gao.blog.dao.FavorReponsitory;
import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.Favor;
import com.gao.blog.pojo.Type;
import com.gao.blog.service.BlogService;
import com.gao.blog.service.TypeService;
import com.gao.blog.vo.BlogQuery;
import com.gao.blog.vo.BlogVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TypeShowController {
    static long tid;
    @Autowired
    TypeService typeService;
    @Autowired
    BlogService blogService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    FavorReponsitory favorReponsitory;

    /**
     * 类型跳转
     * @param id
     * @param model
     * @param pageable
     * @return
     */
    @GetMapping("/types/{id}")
    public String types(@PathVariable Long id, Model model, @PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable){
        List<Type> types = typeService.listTypeTop(10000);
        if (id == -1){
            id = types.get(0).getId();
        }
        tid = id;
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types",types);
        model.addAttribute("page",blogService.listBlog(id,pageable));
        model.addAttribute("activeTypeId",id);
        return "types";
    }

    /**
     * types页面分页用的
     * @param model
     * @param pageable
     * @return
     */
    @GetMapping("/typess")
    public String type(Model model, @PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable){
        List<Type> types = typeService.listTypeTop(10000);
        model.addAttribute("types",types);
        model.addAttribute("page",blogService.listBlogTypeId(pageable));
        model.addAttribute("activeTypeId",tid);
        return "types";
    }
}
