package com.gao.blog.service;

import com.gao.blog.NotFoundException;
import com.gao.blog.dao.TagRepository;
import com.gao.blog.pojo.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService{

    @Autowired
    TagRepository tagRepository;

    /**
     *
     * @param tag
     * @return
     */
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    /**
     * 获取tag
     * @param id
     * @return
     */
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    /**
     * 根据名字查找tag
     * @param name
     * @return
     */
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    /**
     * 分页
     * @param pageable
     * @return
     */
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    /**
     * 更新tag
     * @param id
     * @param tag
     * @return
     */
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.getOne(id);
        if (t==null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(tag,t);
        return tagRepository.save(t);
    }

    /**
     * 删除tag
     * @param id
     */
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    /**
     * 获取全部tag
     * @return
     */
    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    /**
     * 根据字符串ids获取Tag
     * @param ids
     * @return
     */
    @Override
    public List<Tag> listTag(String ids) {
        convertToList(ids);
        return tagRepository.findAllById(convertToList(ids));
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return tagRepository.findTop(pageable);
    }

    /**
     * 把字符串1,2,3转换为list【1,2,3】
     * @param ids
     * @return
     */
    private List<Long> convertToList(String ids){
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null){
            String[] idarray = ids.split(",");
            for (int i=0;i < idarray.length;i++){
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }
}
