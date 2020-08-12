package com.gao.blog.dao;

import com.gao.blog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 这里用了SpringDataJPA的，目前还没学,需要研究
 */
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password);

    User findByUsername(String username);

    User findByEmail(String email);

    User findByPhone(String phone);

}
