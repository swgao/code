package com.gao.blog.web;

import com.gao.blog.dao.UserRepository;
import com.gao.blog.pojo.User;
import com.gao.blog.service.TaService;
import com.gao.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TaController {
    @Autowired
    TaService taService;
    @Autowired
    UserRepository userRepository;
    static long userID;
    /**
     * 他的主页跳转
     * @param userId
     * @param pn
     * @param model
     * @return
     */
    @RequestMapping("/ta/{userId}")
    public String ta(@PathVariable("userId") long userId, @RequestParam(defaultValue = "1") Integer pn, Model model){
        // user基本信息，user的文章分页
        taService.findData(userId, pn,model);
        return "ta/index";
    }
}
