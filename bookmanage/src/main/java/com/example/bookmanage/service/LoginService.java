package com.example.bookmanage.service;

import com.example.bookmanage.config.jwt.JwtUtil;
import com.example.bookmanage.entity.impl.LoginUser;
import exception.ServiceException;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 *
 * @Author zheng
 * @Date 2023/03/29 09:25:03
 * @Version 1.0
 */
@Component
public class LoginService {

    @Autowired
    private JwtUtil jwTutil;

    @Resource
    private AuthenticationManager authenticationManager;


    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public Map login(String username, String password) {
        Map<String,Object> tokenData=new HashMap<>();
        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            // 生成token
            String token=jwTutil.createToken(loginUser);
            tokenData.put("token",token);
            tokenData.put("code","200");
            tokenData.put("message","登录成功");
            return tokenData;
        } catch (Exception e) {
            if(e instanceof BadCredentialsException){
                throw new RuntimeException("用户名密码不匹配！");
            }
            else throw new ServiceException(e.getMessage());
        }


    }


}

