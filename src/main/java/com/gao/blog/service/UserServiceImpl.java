package com.gao.blog.service;

import com.gao.blog.dao.FavorReponsitory;
import com.gao.blog.dao.FollowsRepository;
import com.gao.blog.dao.UserRepository;
import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.Favor;
import com.gao.blog.pojo.Follow;
import com.gao.blog.pojo.User;
import com.gao.blog.util.DigestHelper;
import com.gao.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FavorReponsitory favorReponsitory;
    @Autowired
    private FollowsRepository followsRepository;
    /**
     * 用户检查业务实现
     * @param username
     * @param password
     * @return
     */
    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username,password);
        return user;
    }

    /**
     * 修改密码业务实现
     * @param oldPassword
     * @param password
     * @return
     */
    @Transactional
    @Override
    public Result updatePasswrod(String oldPassword, String password) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        // 获取用户
        User user1 = userRepository.getOne(user.getId());
        String salt = user1.getSalt();
        System.out.println(oldPassword);
        String r = DigestHelper.getSha1(oldPassword+salt);
        if (r.equals(user1.getPassword())){
            user1.setSalt(UUID.randomUUID().toString());
            String newpassword = DigestHelper.getSha1(password+user1.getSalt());
            user1.setPassword(newpassword);
            userRepository.save(user1);
            return Result.of(200);
        }else{
            return Result.of(300);
        }
    }
    @Transactional
    @Override
    public Result updateProfile(String nickname, String sign) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        // 获取用户
        User user1 = userRepository.getOne(user.getId());
        if (user1!=null){
            user1.setNickname(nickname);
            user1.setSign(sign);
            userRepository.save(user1);
            session.setAttribute("user",user1);
        }else{
            return Result.of(404);
        }

        return null;
    }

    /**
     * 点击喜欢处理
     * @param blogId
     * @param session
     * @return
     */
    @Override
    public String saveFavor(long blogId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        User user1 = userRepository.getOne(user.getId());
        if (user == null){
            return "redirect:/admin/login";
        }
        if (favorReponsitory.find(user1.getId(),blogId) != null){
            Favor one = favorReponsitory.find(user1.getId(),blogId);
            if (one.isIf_like()){
                one.setIf_like(false);
            }else{
                one.setIf_like(true);
            }
            favorReponsitory.save(one);
            System.out.println(111);
        }else{
            Favor favor = new Favor();
            Blog blog = new Blog();
            blog.setId(blogId);
            favor.setBlog(blog);
            favor.setCreteTime(new Date());
            favor.setUser(user1);
            favor.setIf_like(true);
            favorReponsitory.save(favor);
        }
        return null;
    }

    /**
     * 处理点击关注
     * @param userId
     * @return
     */
    @Override
    public Result saveFollow(long userId) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user==null){
            return Result.of(0,"未登录");
        }
        if (followsRepository.find(user.getId(),userId)!=null){
            return Result.of(3,"已经关注了，无需重复");
        }
        Follow follows = new Follow();
        follows.setCreateTime(new Date());
        follows.setSource(user);
        User target = new User();
        target.setId(userId);
        follows.setTarget(target);
        followsRepository.save(follows);
        return Result.of(1,"关注成功");
    }

    @Override
    public Result followCheck(long userId) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user==null){
            return Result.of(0,"未登录");
        }
        Follow follow = followsRepository.find(user.getId(), userId);
        if (follow == null){
            return Result.of(1,"未关注");
        }
        return Result.of(2,"已关注");
    }


}
