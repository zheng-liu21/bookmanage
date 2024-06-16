package com.example.bookmanage.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 跨域过滤器
 *
 * @Author zheng
 * @Date 2023/03/28 18:10:35
 * @Version 1.0
 */
@Component
public class CorsFilter extends OncePerRequestFilter {
    /**
     * @Author zheng
     * @Description
     * @Date  2023/3/27 18:38
     * @Param [request, response, filterChain]
     * @return void
     **/
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");//允许http:/www.xxx.com域发起跨域请求
        response.setHeader("Access-Control-Allow-Credentials", "true");//设置允许Cookie
        response.setHeader("Access-Control-Allow-Methods", "PUT, POST, GET, OPTIONS, DELETE");//允许跨域请求的方法
        response.setHeader("Access-Control-Max-Age", "10");//设置在3600秒内不需要再发送预检验请求
        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, Authorization,X-Content-Type-Options");//请求头可包含的内容
        response.setHeader("XDomainRequestAllowed","1");//
        //使前端能够获取到自定义响应报头
        response.setHeader("Access-Control-Expose-Headers","download-status,download-filename,download-message");


        if (request.getMethod().equals("OPTIONS"))
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        else
            filterChain.doFilter(request, response);

    }
}
