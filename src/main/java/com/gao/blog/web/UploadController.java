package com.gao.blog.web;


import com.gao.blog.dto.FileDTO;
import com.gao.blog.util.FileDownload;
import com.gao.blog.vo.Result;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.*;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        File file = new File(StoreRootPath,request.getServletPath());
        try {
            FileDownload.forward(request,response,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "访问图片";
    }

    /**
     * 处理博客添加的图片路径
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/store/blogImage/**")
    public Object test(HttpServletRequest request, HttpServletResponse response){
        System.out.println(request.getServletPath());

        File file = new File(StoreRootPath,request.getServletPath());
        try {
            FileDownload.forward(request,response,file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "访问图片";
    }

    /**
     * 保存博客添加的图片
     * @param file
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/file/upload")
    public FileDTO blogupload(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws IOException {
        Date date = new Date();
        String filename = "/store/blogImage/"
                +new SimpleDateFormat("yyyy").format(date)+"/"
                +new SimpleDateFormat("MM").format(date)+"/"
                +new SimpleDateFormat("dd").format(date)+"/"
                + UUID.randomUUID().toString()+".jpg";
        File local = new File(StoreRootPath,filename);
        File dir = local.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File tempFile = new File(StoreRootPath,filename);
        Thumbnails.of(file.getInputStream()).
                size(500,510).
                outputFormat("jpg").
                toFile(tempFile);
        System.out.println(tempFile);
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl(filename);
        return fileDTO;
    }
}

