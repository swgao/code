package com.gao.blog.interceptor;

import com.gao.blog.pojo.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 后台权限限制
 */
public class SysInterceptor extends HandlerInterceptorAdapter {
    /**
     * 进入拦截器后首先进入的方法
     * 返回false则不再继续执行
     * 返回true则继续执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)throws Exception
    {
        System.out.println("我是拦截器：我证明我进来了");
        HttpSession session=request.getSession();
        User userInfo = (User)session.getAttribute("superUser");
        if(userInfo==null)
        {
            System.out.println("我证明用户没有登录");
            response.sendRedirect("/admins/login");
            return false;
        }
        System.out.println("我证明用户已经登录");
        return  true;
    }
    /**
     * 生成视图时执行，可以用来处理异常，并记录在日志中
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object arg2, Exception exception){
        //-----------------//
    }

    /** -
     * 生成视图之前执行，可以修改ModelAndView
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object arg2, ModelAndView arg3)
            throws Exception{
        //----------------------------//
    }
}
