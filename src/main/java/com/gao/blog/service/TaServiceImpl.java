package com.gao.blog.service;

import com.gao.blog.dao.BlogRepository;
import com.gao.blog.dao.CommentRepository;
import com.gao.blog.dao.FavorReponsitory;
import com.gao.blog.dao.UserRepository;
import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.User;
import com.gao.blog.vo.BlogVO;
import com.gao.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaServiceImpl implements TaService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    FavorReponsitory favorReponsitory;
    @Autowired
    CommentRepository commentRepository;
    @Override
    public void findData(long userId, int page, Model mav) {
        User user = userRepository.getOne(userId);
        mav.addAttribute("user", user);
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, 100, sort);
        Page<Blog> blogs = blogRepository.tt(userId, pageable);
        mav.addAttribute("page", blogs);
        // 拿到博客
        List<Blog> lists = new ArrayList();
        // 遍历
        for (int i = 0; i < blogs.getContent().size(); i++) {
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
            BeanUtils.copyProperties(a, v);
            //添加到列表
            lists.add(v);

        }
        mav.addAttribute("pages", lists);
    }
}
