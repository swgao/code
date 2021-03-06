package com.gao.blog.service;

import com.gao.blog.NotFoundException;
import com.gao.blog.dao.BlogRepository;
import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.Type;
import com.gao.blog.pojo.User;
import com.gao.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    BlogRepository blogRepository;
    public static long id;
    public static long typeId;
    /**
     * 根据id获取博客
     * @param id
     * @return
     */
    @Override
    public Blog getBlog(Long id) {
        blogRepository.updateViews(id);
        return blogRepository.getOne(id);
    }

    /**
     * 分页
     * @param pageable
     * @param blog
     * @return
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }
                if (blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    /**
     * type页面分页用的
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlogTypeId(Pageable pageable) {
        return listBlog(typeId,pageable);
    }

    /**
     * tag分页查询
     * @param pageable
     * @param tagId
     * @return
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable, Long tagId) {
        id =  tagId;
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    /**
     * type页面数据
     * @param typesId
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(Long typesId, Pageable pageable) {
        typeId =  typesId;
        return blogRepository.userListType(typesId,pageable);
    }

    /**
     * tag页面分页用的
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlogTagId(Pageable pageable) {
        return listBlog(pageable,id);
    }


    /**
     * 保存博客
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else{
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    /**
     * 更新博客
     * @param id
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if (b == null){
            throw new NotFoundException("该博客不存在");
        }
        String[] arr = {"createTime"};
        BeanUtils.copyProperties(blog,b,arr);
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    /**
     * 删除博客
     * @param id
     */
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.test(pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, String query) {
        return blogRepository.findByQuery(query,pageable);
    }

    /**
     * 根据userId查询blog分页数据
     * @param pageable
     * @param userId
     * @return
     */
    @Override
    public Page<Blog> homeBlog(Pageable pageable, Long userId) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("user");
                return cb.equal(join.get("id"),userId);
            }
        },pageable);
    }

    /**
     * 查询最新推荐数据
     * @param size
     * @return
     */
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);

    }

    /**
     * 查询没年的blog
     * @return
     */
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String,List<Blog>> map = new HashMap<>();
        for (String year : years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    /**
     * 查询blog总数
     * @return
     */
    @Override
    public Long countBlog() {
        return blogRepository.count();
    }
}
