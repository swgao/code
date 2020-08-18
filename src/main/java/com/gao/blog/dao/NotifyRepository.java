package com.gao.blog.dao;

import com.gao.blog.pojo.Notify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotifyRepository extends JpaRepository<Notify,Long> {
    /**
     * 查询当前用户有多少条通知
     * @param userId
     * @param pageable
     * @return
     */
    @Query("from Notify n where n.user.id=?1")
    Page<Notify> findByUser(Long userId, Pageable pageable);
}
