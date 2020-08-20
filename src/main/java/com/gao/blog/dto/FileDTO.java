package com.gao.blog.dto;

import lombok.Data;
/**
 * 用户接收和返回enditormd的上传返回值
 */
@Data
public class FileDTO {
    private int success;
    private String message;
    private String url;
}
