package com.gao.blog.service;

import com.gao.blog.dao.BlogRepository;
import com.gao.blog.dao.UserRepository;
import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.User;
import com.gao.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public class TaServiceImpl implements TaService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BlogRepository blogRepository;
    @Override
    public void findData(long userId, int page, Model mav) {
        User user = userRepository.getOne(userId);
        mav.addAttribute("user",user);

        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(page-1,10,sort);
        Page<Blog> data = blogRepository.tt(userId,pageable);

        mav.addAttribute("page",data);
    }
}
