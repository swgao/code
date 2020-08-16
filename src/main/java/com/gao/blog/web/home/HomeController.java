package com.gao.blog.web.home;

import com.gao.blog.dao.FavorReponsitory;
import com.gao.blog.dao.UserRepository;
import com.gao.blog.pojo.User;
import com.gao.blog.service.*;
import com.gao.blog.vo.BlogQuery;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @Value("${STORE_ROOT_PATH}")
    String StoreRootPath; // 寻存储根目录

    /**
     * 个人主页
     * @param model
     * @param pageable
     * @return
     */
    @RequestMapping({"/index","","/"})
    public String index(@RequestParam(defaultValue = "1") Integer pages,Model model, @PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, HttpSession session){
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
        User user = (User) session.getAttribute("user");
        // 获取用户
        User user1 = userRepository.getOne(user.getId());
        model.addAttribute("page",blogService.homeBlog(pageable,user1.getId()));
//        model.addAttribute("types",typeService.listType());
        // 我的关注
        homeService.follows(pages,model);
        return "home/index";
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
}
