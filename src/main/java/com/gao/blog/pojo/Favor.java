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
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private User user;
    @ManyToOne
    @JoinColumn(name = "blog_id")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Blog blog;
    private Date creteTime;
    private boolean if_like;
}
