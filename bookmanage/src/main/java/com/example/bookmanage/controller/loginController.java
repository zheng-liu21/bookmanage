package com.example.bookmanage.controller;

import com.example.bookmanage.entity.user;
import com.example.bookmanage.service.LoginService;
import com.example.bookmanage.service.userService;
import com.example.bookmanage.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class loginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private userService userservice;
    @RequestMapping("/login")
    public ResultUtil login(HttpServletRequest request) {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        Map<String,Object> map=new HashMap<>();
        map = loginService.login(username, password);
//        map.put("token",token);
        return ResultUtil.ok().data(map);
//        user user =userservice.findUserByName(username);
//        Map<String,Object> tokenData=new HashMap<>();
//        if (user != null) {
//            if (password.equals(user.getPassword())) {
////                String token = JwtUtil.sign(username);
////                tokenData.put("token",token);
//                tokenData.put("code","200");
//                tokenData.put("message","登录成功");
//                return ResultUtil.ok().data(tokenData);
//            } else{
//                return ResultUtil.error().message("密码错误！");}
//        } else{
//            return ResultUtil.error().message("用户名不存在！");}
    }

}
