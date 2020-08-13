package com.gao.blog.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 下载文件 创建人：Master.Xia 创建时间：2018年02月16日
 * 
 * @version
 */
public class FileDownload {

    /**
     * 请求转发到一个指定的文件，就像浏览器直接访问这个文件一样，不会弹框提示
     * @param request (请求)
     * @param response (响应)
     * @param file (访问的文件)
     * @throws Exception (异常)
     */
    public static void forward(HttpServletRequest request,HttpServletResponse response,File file)throws Exception {
    	forward(request, response, file, null, false);
    }
    
    /**
     * 请求转发到一个指定的文件，就像浏览器直接访问这个文件一样，如果遇到浏览器不认识的文件类型，则会下载它，支持断点续传。
     * 当isAttachment为true时会有下载提示框
     * @param request (请求)
     * @param response (响应)
     * @param file (访问的文件)
     * @param name (下载时在浏览器显示的名字)
     * @param isAttachment (浏览器是否弹出下载框)
     * @throws Exception (异常)
     */
    public static void forward(HttpServletRequest request,HttpServletResponse response,File file,String name,boolean isAttachment)throws Exception {

        Path path = Paths.get(file.getAbsolutePath());

        int length = (int)file.length();
        int start = 0;
        int end = length - 1;

        String range = request.getHeader("Range");
        if(range!=null&&range.length()>0) {
            range = range.toLowerCase();    //RANGE:BYTES=100-500
            range = range.replaceAll("\\s*", ""); //去除字符串中所有的不可见字符
            if(range.contains("bytes=")) {//bytes=100
                if(range.contains("-")) {
                    String a = range.substring(6, range.indexOf("-"));
                    start = Integer.parseInt(a);
                    if(range.charAt(range.length()-1)!='-') {
                        String b = range.substring(range.indexOf("-")+1);
                        end = Integer.parseInt(b);
                    }
                }else {
                    String a = range.substring(6);
                    start = Integer.parseInt(a);
                }
            }
        }

        int contentLength = end - start + 1;
        response.reset();
        response.setHeader("Accept-Ranges", "bytes");
        response.setContentType(Files.probeContentType(path));
        response.setHeader("Content-Range",String.format("bytes %s-%s/%s", start, end, length));
        response.setHeader("Content-Length", String.format("%s", contentLength));

        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        
        if(isAttachment) {
        	//如果是下载，则弹框提示，解决乱码问题
            String info = request.getHeader("User-Agent").toLowerCase();
            String showName = file.getName();
            if(name!=null&&name.length()>0)showName=name;
            if (info.contains("firefox")) { // Firefox
                showName = new String(showName.getBytes(), "ISO-8859-1");
            } else { // IE,Chrome
                showName = URLEncoder.encode(showName, "UTF-8");
            }
            response.setHeader("Content-Disposition", "attachment; filename=\""+ showName + "\"");
        }
        
        //数据传输
        InputStream in = null;
        try{
            in = new FileInputStream(file);
            int len,remain = contentLength;
            byte[] buffer = new byte[1024*8];
            OutputStream out = response.getOutputStream();
            int plan = start;
            while(plan>0){
                plan -= in.skip(plan);
            }
            while((len=in.read(buffer))!=-1 && remain > 0) {
                out.write(buffer, 0, (remain<len? remain:len)  );
            }
        }catch (Exception e) { 
            throw e;
        }finally {
            try {in.close(); } catch (Exception e2) { }

        }
    }
    
}