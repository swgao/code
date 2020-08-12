package com.gao.blog.service;

import com.gao.blog.pojo.Tag;
import com.gao.blog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TagService {
    /**
     * 添加一个Tag
     * @param tag
     * @return
     */
    Tag saveTag(Tag tag);

    /**
     * 获取Tag
     * @param id
     * @return
     */
    Tag getTag(Long id);

    /**
     * 根据名称查询Tag
     * @param name
     * @return
     */
    Tag getTagByName(String name);

    /**
     * 查询全部Tag
     * @param pageable
     * @return
     */
    Page<Tag> listTag(Pageable pageable);

    /**
     * 修改Type
     * @param id
     * @param tag
     * @return
     */
    Tag updateTag(Long id,Tag tag);

    /**
     * 删除一个Tag
     * @param id
     */
    void deleteTag(Long id);

    /**
     * 获取全部tag
     * @return
     */
    List<Tag> listTag();

    /**
     * 根据ids获取tag  1,2,3
     * @param ids
     * @return
     */
    List<Tag> listTag(String ids);

    List<Tag> listTagTop(Integer sizw);
}
