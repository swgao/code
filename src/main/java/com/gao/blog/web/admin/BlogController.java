package com.gao.blog.web.admin;

import com.gao.blog.dao.BlogRepository;
import com.gao.blog.pojo.Blog;
import com.gao.blog.pojo.User;
import com.gao.blog.service.BlogService;
import com.gao.blog.service.TagService;
import com.gao.blog.service.TypeService;
import com.gao.blog.vo.BlogQuery;
import com.gao.blog.vo.Result;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Controller
@RequestMapping("/admin")
public class BlogController {
    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Value("${STORE_ROOT_PATH}")
    String StoreRootPath; // 寻存储根目录
    @Autowired
    private BlogService blogService;

    @Autowired
    TypeService typeService;
@Autowired
BlogRepository blogRepository;
    @Autowired
    TagService tagService;

    /**
     * 后台博客页面
     * @param model
     * @param pageable
     * @param blog
     * @return
     */
    @GetMapping("/blogs")
    public String blogs(Model model, @PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog){
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        model.addAttribute("types",typeService.listType());
        return LIST;
    }

    /**
     * 查询博客
     * @param model
     * @param pageable
     * @param blog
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(Model model, @PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blog){
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        // 返回fragment对应的
        return "admin/blogs :: blogList";
    }

    /**
     * 跳转博客发布页面
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String input(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());

        return INPUT;
    }

    /**
     * 提取公共的
     * @param model
     */
    private void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }

    /**
     * 编辑博客，根据id
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable("id")Long id, Model model){
        setTypeAndTag(model);
        Blog blog = blogRepository.getOne(id);
        blog.init();
        model.addAttribute("blog",blogService.getBlog(id));
        return INPUT;
    }

    /**
     * 编辑完的保存处理
     * @param blog
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/blogs")
    public String post(Blog blog, HttpSession session, RedirectAttributes attributes, HttpServletRequest request) throws IOException, ServletException {
        blog.setUser((User) session.getAttribute("user"));
        System.out.println(blog.getFlag());
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Part file = request.getPart("file");
        String filename = "";
        try {
            int tow = 300;
            int toh = 150;
            filename = "/store/temp/"+ UUID.randomUUID().toString()+".jpg";
            File tempFile = new File(StoreRootPath,filename);// 临时文件
            File parent = tempFile.getParentFile();
            if (!parent.exists()){
                parent.mkdirs();
            }
            Thumbnails.of(file.getInputStream()).
                    size(tow,toh).
                    outputFormat("jpg").
                    toFile(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(filename);
        blog.setFirstPicture(filename);
        Blog b = blogService.saveBlog(blog);
        if (b == null){
            attributes.addFlashAttribute("message","操作失败");
        }else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/home/index";
    }

    /**
     * 博客更新
     * @param blog
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/blogs/update")
    public String update(Blog blog, HttpSession session, RedirectAttributes attributes,HttpServletRequest request) throws IOException, ServletException {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Part file = request.getPart("file");
        System.out.println(file.getSize());
        if (file.getSize()==0){
            String filename = request.getParameter("file1");
            blog.setFirstPicture(filename);
        }else{
            System.out.println(file);
            String filename = "";
            try {
                int tow = 300;
                int toh = 150;
                filename = "/store/temp/"+ UUID.randomUUID().toString()+".jpg";
                File tempFile = new File(StoreRootPath,filename);// 临时文件
                File parent = tempFile.getParentFile();
                if (!parent.exists()){
                    parent.mkdirs();
                }
                Thumbnails.of(file.getInputStream()).
                        size(tow,toh).
                        outputFormat("jpg").
                        toFile(tempFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(filename);
            blog.setFirstPicture(filename);
        }
        Blog b = blogService.updateBlog(blog.getId(),blog);
        if (b == null){
            attributes.addFlashAttribute("message","操作失败");
        }else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return"redirect:/home";
    }

    /**
     * 博客删除
     * @param attributes
     * @param id
     * @return
     */
    @GetMapping("/blogs/{id}/delete")
    public String delete(RedirectAttributes attributes,@PathVariable("id") Long id){
        blogService.deleteBlog(id);
        return "redirect:/home/index";
    }


}
