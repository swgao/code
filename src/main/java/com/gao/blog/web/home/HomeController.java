package com.gao.blog.web.home;

import com.gao.blog.dao.FavorReponsitory;
import com.gao.blog.dao.NotifyRepository;
import com.gao.blog.dao.UserRepository;
import com.gao.blog.pojo.Notify;
import com.gao.blog.pojo.User;
import com.gao.blog.service.*;

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


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private BlogService blogService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;
    @Autowired
    UserService userService;
    @Autowired
    FavorReponsitory favorReponsitory;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HomeService homeService;
    @Autowired
    NotifyRepository notifyRepository;
    @Value("${STORE_ROOT_PATH}")
    String StoreRootPath; // 寻存储根目录

    /**
     * 个人主页
     * @param model
     * @param pageable
     * @return
     */
    @RequestMapping({"/index","","/"})
    public String index(Model model, @PageableDefault(size = 4,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable, HttpSession session){

        User user = (User) session.getAttribute("user");
        // 获取用户
        User user1 = userRepository.getOne(user.getId());
        model.addAttribute("page",blogService.homeBlog(pageable,user1.getId()));
//        model.addAttribute("types",typeService.listType());
        // 我的关注
        model.addAttribute("follows",homeService.follows(pageable));
        // 我的粉丝
        model.addAttribute("fans",homeService.fans(pageable));
        // 通知
        model.addAttribute("count", homeService.count());
        model.addAttribute("data",homeService.notifies(pageable));
        // 我的喜欢
        model.addAttribute("love",favorReponsitory.findByBlog(user.getId(),pageable));
        return "home/index";
    }

    /**
     * 动态加载我的关注 分页
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping("/follows")
    public String follows( @PageableDefault(size = 4,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        // 我的关注
        model.addAttribute("follows",homeService.follows(pageable));
        return "home/index :: followsList";
    }

    /**
     * 动态加载 我的粉丝分页
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping("/fans")
    public String fans( @PageableDefault(size = 4,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        // 我的粉丝
        model.addAttribute("fans",homeService.fans(pageable));
        return "home/index :: fansList";
    }

    /**
     * 我的通知 分页
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping("/datas")
    public String datas( @PageableDefault(size = 4,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        // 通知
        model.addAttribute("data",homeService.notifies(pageable));
        return "home/index :: datasList";
    }

    /**
     * 图片提交
     * @param x
     * @param y
     * @param width 宽度
     * @param height 高度
     * @param path 地址
     * @return
     */
    @PostMapping("/avatar")
    public String updateAvatar(int x, int y, int width, int height, String path, HttpServletRequest request){
        Date date = new Date();
        String a = "/store/avatar/"
                            +new SimpleDateFormat("yyyy").format(date)+"/"
                            +new SimpleDateFormat("MM").format(date)+"/"
                            +new SimpleDateFormat("dd").format(date)+"/"
                            + UUID.randomUUID().toString()+".jpg";

        try {
            File local = new File(StoreRootPath,a);
            File dir = local.getParentFile();
            if (!dir.exists()){
                dir.mkdirs();
            }
            File file = new File(StoreRootPath,path);
            System.out.println(file);
            // 对图片裁剪
            System.out.println(local);
            Thumbnails.of(file)
                    .sourceRegion(x,y,width,height) // 裁剪
                    .size(width,height)
                    .outputFormat("jpg")    // 后缀
                    .toFile(local); // 保存到的新路径
            User loginUser = (User) request.getSession().getAttribute("user");
            if (loginUser==null){
                return "redirect:/admin";
            }else{
                User user = userRepository.getOne(loginUser.getId());
                user.setAvatar(a);
                request.getSession().setAttribute("user",user);
                userRepository.save(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/home/avatar";
    }
    /**
     * 头像修改页面跳转
     * @return
     */
    @GetMapping("/avatar")
    public String avatar(){
        return "home/avatar";
    }

    /**
     * 基本信息修改页面跳转
     * @return
     */
    @RequestMapping("/profile")
    public String profile(){
        return "home/profile";
    }
    @RequestMapping("/profile/update")
    public Object updateProfile(String nickname,String sign){
        userService.updateProfile(nickname,sign);
        return "redirect:/home/profile";
    }
    /**
     * 密码修改页面跳转
     * @return
     */
    @RequestMapping("/password")
    public String password(){
        return "home/password";
    }

    /**
     * 密码修改
     * @param oldPassword
     * @param password
     * @return
     */
    @RequestMapping("/password/update")
    @ResponseBody
    public Object updatePassword(String oldPassword,String password){
        return userService.updatePasswrod(oldPassword,password);
    }

    /**、
     * 修改邮件页面
     * @return
     */
    @GetMapping("/email")
    public String email(){
        return "home/email";
    }

    /**
     * 点击喜欢处理
     * @param id
     * @param session
     * @return
     */
    @RequestMapping("/like/{id}")
    public String like(@PathVariable Long id,HttpSession session){
        userService.saveFavor(id,session);
        return "redirect:/blog/"+id;
    }

    /**
     * 用户点击通知后，清除他的未读
     * @return
     */
    @ResponseBody
    @RequestMapping("/unNotice")
    public Result unNotice(){
        List<Notify> all = homeService.unNotify();
        for (Notify notify : all) {
            notify.setStatus(1);
            notifyRepository.save(notify);
        }
        return Result.of(200);
    }
}
