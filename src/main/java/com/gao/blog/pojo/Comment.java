package com.gao.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_comment")
public class   Comment {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private String avatar;
    @ManyToOne
    @JoinColumn(name = "comment_by_id")
    private User user_id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @ManyToOne
    private Blog blog;

    @OneToMany(mappedBy = "parentComment")
    List<Comment> replyComments = new ArrayList<>();
    @ManyToOne
    Comment parentComment;

    boolean adminComment;
}
