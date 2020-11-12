package com.crm.web.filter;

import com.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进行登陆验证");

        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;


        String path = request.getServletPath();
        if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path) ){
            //登录页不用拦截
            filterChain.doFilter(req,resp);
        }else{

            HttpSession session = request.getSession();
            User user = (User)session.getAttribute("user");

            //session域中存在用户，放行
            if(user != null){
                filterChain.doFilter(req,resp);
            }else{
                //重定向到登录页
            /*
            使用重定向的原因：
            转发之后，路径会停留在老路径上，而不是跳转之后最新资源的路径
            用户跳转到登录页的同时，将浏览器的地址栏应该自动设置为当前登录页的路径

             */
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }


    }

    @Override
    public void destroy() {

    }
}
