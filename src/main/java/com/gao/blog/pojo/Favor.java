package com.gao.blog.pojo;

import lombok.Data;
import org.hibernate.annotations.Cascade;
import javax.persistence.*;
import java.util.Date;

/**
 * 喜欢实体类
 */
@Data
@Entity
@Table(name = "blog_favor")
public class Favor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;
    private Date createTime;
    private boolean if_like;
}
