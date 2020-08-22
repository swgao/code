package com.gao.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String username;
    private String password;
    private String sign;
    private String email;
    private String avatar;
    private String salt;
    private String phone;
    private Integer type;
    // 账号状态
    private Boolean status;  //0-代表停用 1-代表正常
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private List<Blog> blogs = new ArrayList<>();
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "user")
    private List<Notify> notifies = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "target")
    private List<Follow> follows = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "source")
    private List<Follow> follows1 = new ArrayList<>();
}
