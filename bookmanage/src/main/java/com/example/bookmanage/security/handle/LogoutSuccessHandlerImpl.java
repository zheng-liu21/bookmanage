package com.example.bookmanage.security.handle;

/**
 * 描述
 *
 * @Author zheng
 * @Date 2023/03/29 09:13:30
 * @Version 1.0
 */

import com.alibaba.fastjson2.JSON;
import com.example.bookmanage.config.jwt.JwtUtil;
import com.example.bookmanage.constant.HttpStatus;
import com.example.bookmanage.entity.impl.LoginUser;
import com.example.bookmanage.utils.ResultUtil;
import com.example.bookmanage.utils.StringUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private JwtUtil tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
        }
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSON.toJSONString(ResultUtil.error(true, HttpStatus.SUCCESS, "退出成功")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

