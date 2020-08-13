package com.gao.blog.web;


import com.gao.blog.util.FileDownload;
import com.gao.blog.vo.Result;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class UploadController {

    @Value("${STORE_ROOT_PATH}")
    String StoreRootPath; // 寻存储根目录

    /**
     * 文件上传到临时文件
     * @param size
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("/post/upload")
    @ResponseBody
    public Object save(int size, MultipartFile file, HttpServletRequest request){
        System.out.println(file.getSize());
        System.out.println(size);
        try {
            BufferedImage bi = ImageIO.read(file.getInputStream());
            int width = bi.getWidth();
            int height = bi.getHeight();
            int max = (int) Math.max(width, height);
            int tow = width;
            int toh = height;
            if (max > size) {
                if (width > height) {
                    tow = size;
                    toh = height * size / width;
                } else {
                    tow = width * size / height;
                    toh = size;
                }
            }
            String filename = "/store/temp/"+UUID.randomUUID().toString()+".jpg";
            File tempFile = new File(StoreRootPath,filename);// 临时文件
            File parent = tempFile.getParentFile();
            if (!parent.exists()){
                parent.mkdirs();
            }
            Thumbnails.of(file.getInputStream()).
                    size(tow,toh).
                    outputFormat("jpg").
                    toFile(tempFile);
            return Result.of(1,"上传成功",filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.of(2,"上传失败");
    }

    /**
     * 接收回传的图片
     * @param filename
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/store/temp/{filename}")
    public Object upload(@PathVariable String filename, HttpServletRequest request, HttpServletResponse response){
        File file = new File(StoreRootPath,"/store/temp/"+filename);
        try {
            FileDownload.forward(request,response,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 访问图图片处理
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/store/avatar/**")
    public Object avatar(HttpServletRequest request, HttpServletResponse response){
        System.out.println(request.getServletPath());
        File file = new File(StoreRootPath,request.getServletPath());
        try {
            FileDownload.forward(request,response,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "访问图片";
    }
}
