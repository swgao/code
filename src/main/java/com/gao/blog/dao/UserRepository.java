package com.gao.blog.dao;

import com.gao.blog.pojo.User;
import com.gao.blog.vo.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 这里用了SpringDataJPA的，目前还没学,需要研究
 */
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password);

    User findByUsername(String username);

    User findByEmail(String email);

    User findByPhone(String phone);

    /**
     * 后台-分页查看所有用户
     * @param pageable
     * @return
     */
    Page<User> findAll(Pageable pageable);

    /**
     * 查询数据库中是否有这个用户
     * @param id
     * @return
     */
    @Query("select count(*) from User u where u.id = ?1")
    int findUserNull(long id);


}
