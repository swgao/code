package com.gao.blog.vo;

import java.util.HashMap;

public class Result extends HashMap<String,Object> {

    public static Result of(int code){
        Result er = new Result();
        er.put("code",code);
        return er;
    }
    public static Result of(int code,String msg){
        Result er = new Result();
        er.put("code",code);
        er.put("msg",msg);
        return er;
    }
    public static Result of(int code,String msg,Object data){
        Result er = new Result();
        er.put("code",code);
        er.put("msg",msg);
        er.put("data",data);
        return er;
    }
    @Override
    public Result put(String key,Object value){
        super.put(key,value);
        return this;
    }
}
