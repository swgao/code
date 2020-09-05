package com.gao.blog.web.admin;

import com.gao.blog.pojo.Type;
import com.gao.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * 类型
 */
@Controller
@RequestMapping("/admin")
public class TypeController {
    @Autowired
    private TypeService typeService;

    /**
     * 显示类型的页面
     * @param pageable  springboot自带分页
     * @param model     传参
     * @return
     */
    @GetMapping("/types")
    public String types(@PageableDefault(size = 2,sort = {"id"},direction = Sort.Direction.DESC)
                                    Pageable pageable
                                    , Model model){
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    /**
     * 增加类型页面
     * @return
     */
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    /**
     * 保存类型
     * @param type
     * @param result
     * @param attributes
     * @return
     */
//    @PostMapping("/types")
//    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes){
//
//        Type type1 = typeService.getTypeByName(type.getName());
//        if (type1 != null){
//            result.rejectValue("name","nameError","不能添加重复分类");
//        }
//        if (result.hasErrors()){
//            return "admin/types-input";
//        }
//        Type t = typeService.saveType(type);
//        if (t == null){
//            attributes.addFlashAttribute("message","新增失败");
//        }else {
//            attributes.addFlashAttribute("message","新增成功");
//        }
//        return "redirect:/admin/types";
//    }

    /**
     * 修改类型
     * @param id
     * @param model
     * @return
     */
//    @GetMapping("/types/{id}/input")
//    public String editInput(@PathVariable("id") Long id, Model model){
//        model.addAttribute("type",typeService.getType(id));
//        return "admin/types-input";
//    }

    /**
     *
     * @param type
     * @param result
     * @param id
     * @param attributes
     * @return
     */
//    @PostMapping("/types/{id}")
//    public String editPost(@Valid Type type, BindingResult result,@PathVariable("id") Long id, RedirectAttributes attributes){
//
//        Type type1 = typeService.getTypeByName(type.getName());
//        if (type1 != null){
//            result.rejectValue("name","nameError","不能添加重复分类");
//        }
//        if (result.hasErrors()){
//            return "admin/types-input";
//        }
//        Type t = typeService.updateType(id,type);
//        if (t == null){
//            attributes.addFlashAttribute("message","更新失败");
//        }else {
//            attributes.addFlashAttribute("message","更新成功");
//        }
//        return "redirect:/admin/types";
//    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable("id") Long id){
        typeService.deleteType(id);
        return "redirect:/admin/types";
    }
}
