package com.gao.blog.vo;

import com.gao.blog.pojo.Blog;

public class BlogVO extends Blog {
    private long xihuan;
    private long pinglun;

    public long getXihuan() {
        return xihuan;
    }

    public void setXihuan(long xihuan) {
        this.xihuan = xihuan;
    }

    public long getPinglun() {
        return pinglun;
    }

    public void setPinglun(long pinglun) {
        this.pinglun = pinglun;
    }
}
