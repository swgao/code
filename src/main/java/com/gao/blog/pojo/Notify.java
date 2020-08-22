package com.gao.blog.pojo;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;

/**
 * 通知实体
 */
@Data
@Entity
@Table(name = "blog_notify")
public class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String avatar;
    private String title;
    private String url;
    private Date createTime;
    @Column(columnDefinition = "TEXT")
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 通知到归属者,这是给谁的通知

}
