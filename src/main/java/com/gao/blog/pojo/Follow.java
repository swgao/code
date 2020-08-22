package com.gao.blog.pojo;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;

/**
 * 关注实体类
 */
@Data
@Entity
@Table(name = "blog_follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "source_id")
    private User source; // 发起关注的人
    @ManyToOne
    @JoinColumn(name = "target_id")
    private User target; // 被关注的人
    private Date createTime; // 关注时间
}
