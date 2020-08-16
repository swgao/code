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

import java.util.ArrayList;
import java.util.List;

@Controller
public class TagShowController {

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
    public String types(@PathVariable Long id, Model model, @PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable){
        List<Tag> tags = tagService.listTagTop(10000);
        if (id == -1){
            id = tags.get(0).getId();
        }
        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.listBlog(pageable,id));
        model.addAttribute("activeTypeId",id);
        // 拿到博客
        Page<Blog> blogs = blogService.listBlog(pageable,id);
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
            // 然后把a拷贝到v
            BeanUtils.copyProperties(a,v);
            //添加到列表
            lists.add(v);
            // 查询喜欢的个数
            int favorCount = favorReponsitory.countByBlog(a.getId());
            v.setXihuan(favorCount);
            System.out.println(count);
        }
        model.addAttribute("pages",lists);
        return "tags";
    }
}
