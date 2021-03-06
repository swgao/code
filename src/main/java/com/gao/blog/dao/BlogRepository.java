package com.gao.blog.dao;

import com.gao.blog.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.nio.channels.SelectableChannel;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend = true ")
    List<Blog> findTop(Pageable pageable);
    @Query("select b from Blog b where b.published = 0")
    Page<Blog> test(Pageable pageable);
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query,Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);
    @Query("select function('date_format',b.updateTime,'%Y') from Blog b group by function('date_format',b.updateTime,'%Y') order by 'year' DESC")
    List<String> findGroupYear();
    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1")
    List<Blog> findByYear(String year);

    /**
     * 根据userid查询全部博客
     * @param userId
     * @param pageable
     * @return
     */
    @Query("from Blog a where a.user.id=?1")
    Page<Blog> tt(long userId,Pageable pageable);

    /**
     * 根据typeId查询blog
     * @param id
     * @param pageable
     * @return
     */
    @Query("from Blog b where b.type.id=?1")
    Page<Blog> userListType(long id,Pageable pageable);
}
