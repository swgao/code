package com.gao.blog.service;

import com.gao.blog.dao.UserRepository;
import com.gao.blog.pojo.User;
import com.gao.blog.util.DigestHelper;
import com.gao.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

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
        String r = DigestHelper.getSha1(oldPassword+salt);
        if (r.equals(user1.getPassword())){
            user1.setSalt(UUID.randomUUID().toString());
            String newpassword = DigestHelper.getSha1(password+user1.getSalt());
            user1.setPassword(newpassword);
            userRepository.save(user1);
        }else{
            Result.of(300);
        }
        return Result.of(200);
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
}
