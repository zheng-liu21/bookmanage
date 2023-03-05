package com.example.bookmanage.controller;

import com.example.bookmanage.entity.user;
import com.example.bookmanage.service.userService;
import com.example.bookmanage.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class userController {
    @Autowired
    private userService userservice;
    @RequestMapping("/findAllUser")
    public ResultUtil findAllUser(HttpServletRequest request, Map<String,Object> map){
        List<user> adminList=userservice.findAlluser();
        map.put("adminList",adminList);
        return ResultUtil.ok().data(map);
    }
}
