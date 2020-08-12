package com.gao.blog.service;

import com.gao.blog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface TypeService {
    /**
     * 添加一个Type
     * @param type
     * @return
     */
    Type saveType(Type type);

    /**
     * 获取Type
     * @param id
     * @return
     */
    Type getType(Long id);

    /**
     * 根据名称查询Type
     * @param name
     * @return
     */
    Type getTypeByName(String name);

    /**
     * 查询全部Type
     * @param pageable
     * @return
     */
    Page<Type> listType(Pageable pageable);

    /**
     * 修改Type
     * @param id
     * @param type
     * @return
     */
    Type updateType(Long id,Type type);

    /**
     * 删除一个Type
     * @param id
     */
    void deleteType(Long id);

    /**
     * 拿到所有Type
     * @return
     */
    List<Type> listType();

    List<Type> listTypeTop(Integer size);

}
