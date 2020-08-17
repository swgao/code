package com.gao.blog.web;

import com.gao.blog.dao.CommentRepository;
import com.gao.blog.dao.FavorReponsitory;
import com.gao.blog.dao.TagRepository;
import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.Tag;
import com.gao.blog.pojo.Type;
import com.gao.blog.service.BlogService;
import com.gao.blog.service.TagService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TagShowController {
    static long cid;
    @Autowired
    TagService tagService;
    @Autowired
    BlogService blogService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    FavorReponsitory favorReponsitory;

    /**
     * 标签跳转
     * @param id
     * @param model
     * @param pageable
     * @return
     */
    @GetMapping("/tags/{id}")
    public String types(@PathVariable("id") Long id, Model model, @PageableDefault(size = 1,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable){
        List<Tag> tags = tagService.listTagTop(10000);
        if (id == -1){
            id = tags.get(0).getId();
        }
        cid= id;
        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.listBlog(pageable,id));
        model.addAttribute("activeTypeId",id);
        return "tags";
    }

    /**
     * 分页跳转的
     * @param model
     * @param pageable
     * @return
     */
    @GetMapping("/tagg")
    public String type(Model model, @PageableDefault(size = 1,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable){
       model.addAttribute("page",blogService.listBlogTagId(pageable));
        List<Tag> tags = tagService.listTagTop(10000);
        model.addAttribute("tags",tags);
        model.addAttribute("activeTypeId",cid);
        return "tags";
    }
}
