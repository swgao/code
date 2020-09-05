package com.gao.blog.web.test;

import com.gao.blog.pojo.Tag;
import com.gao.blog.service.TagService;
import com.gao.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 后台类型处理
 */
@Controller
@RequestMapping("/admins")
public class TagsController {

    @Autowired
    private TagService tagService;

    /**
     * 后台标签页面跳转和传参
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/tagsList")
    public String tags(@PageableDefault(size = 10,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", tagService.listTag(pageable));
        return "admins/tags-list";
    }
    /**
     * 跳转类型修改页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}/input")
    public String editInputTags(@PathVariable("id") Long id, Model model){
        model.addAttribute("tag",tagService.getTag(id));
        return "admins/tag-update";
    }

    /**
     * 后台修改类型处理
     * @param tag
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("/tags/{id}")
    public Result editTags(@Valid Tag tag, @PathVariable("id") Long id){

        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null){
            return Result.of(500,"不能添加重复分类");
        }
        Tag t = tagService.updateTag(id,tag);
        if (t == null){
            return Result.of(404,"更新失败");
        }else {
            return Result.of(200,"更新成功");
        }
    }

    /**
     * 后台添加标签处理
     * @param tag
     * @return
     */
    @ResponseBody
    @PostMapping("/addTags")
    public Result addTags(@Valid Tag tag){

        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null){
            return Result.of(500,"不能重复添加");
        }
        Tag t = tagService.saveTag(tag);
        if (t == null){
            return Result.of(201,"新增失败");
        }else {
            return Result.of(200,"新增成功");
        }
    }

}
