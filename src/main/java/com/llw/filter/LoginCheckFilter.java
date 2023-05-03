package com.llw.filter;

import com.alibaba.fastjson.JSON;
import com.llw.common.BaseContext;
import com.llw.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的URI
        String requestURI = request.getRequestURI();
//        log.info("本次请求为:{}", requestURI);

        //2.判断本次请求是否需要处理（是否登录）
        //定义不需要处理的请求路径
        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**", //静态资源不用处理
                "/front/**",
                "/user/sendMsg", //移动端验证码
                "/user/login"
        };
        boolean check = check(urls, requestURI);

        //3.如果不需要处理，则直接放行
        if (check) {
            filterChain.doFilter(request, response);
//            log.info("本次请求不需要处理");
            return;
        }

        //4-1.判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            Long empId = (Long) request.getSession().getAttribute("employee");
            // 给当前线程添加该用户的id
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
//            log.info("用户已登录，id为{}", request.getSession().getAttribute("employee"));
            return;
        }

        //4-2.判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("user") != null) {
            Long userId = (Long) request.getSession().getAttribute("user");
            // 给当前线程添加该用户的id
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
//            log.info("用户已登录，id为{}", request.getSession().getAttribute("employee"));
            return;
        }

        //5.如果未登录则返回未登录，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
//        log.info("用户未登录");

    }

    /**
     * 检查本次请求是否需要放行
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI))
                return true;
        }
        return false;
    }
}
