package com.example.bookmanage.controller;

import com.example.bookmanage.config.security.SecurityUtils;
import com.example.bookmanage.entity.impl.LoginUser;
import com.example.bookmanage.entity.user;
import com.example.bookmanage.service.userService;
import com.example.bookmanage.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class userController {
    @Autowired
    private userService userservice;

    @PreAuthorize("@ss.hasPermi('sys:user:findAllUser')")
    @RequestMapping("/findAllUser")
    public ResultUtil findAllUser(HttpServletRequest request,Map<String,Object> map){
        String query=request.getParameter("query");
        user queryuser=new user();
        queryuser.setUser_name(query);
        //截取List查询结果分页
        int pagenum= Integer.parseInt(request.getParameter("pagenum"));
        int pagesize= Integer.parseInt(request.getParameter("pagesize"));
        int count=0;
        List<user> userList=userservice.findAlluser(queryuser);
        for(com.example.bookmanage.entity.user user : userList){
            user.setTouxiag_url("http://localhost:8083/"+user.getTouxiag_url());
        }
        if(userList!=null && userList.size()>0){
            for(com.example.bookmanage.entity.user user : userList){
                if(user.getState().equals("1"))
                    user.setState_bool(true);
                else
                    user.setState_bool(false);
            }
            count=userList.size();
            int fromIndex=(pagenum-1)*pagesize;
            int toIndex=pagenum*pagesize;
            if(toIndex>count)
                toIndex=count;
            List<user> pageUserList=userList.subList(fromIndex,toIndex);
            map.put("userList",pageUserList);
            map.put("pagenum",pagenum);
        }
        map.put("total",userList.size());
        return ResultUtil.ok().data(map);
    }
    @PreAuthorize("@ss.hasPermi('sys:user:updateUser')")
    @RequestMapping("/updateUser")
    public ResultUtil updateUser(user user){
        if(user.getState_bool()!=null&&user.getState_bool().equals(false))
            user.setState("0");
        else user.setState("1");
        userservice.updateUser(user);
        return ResultUtil.ok();
    }
    @PreAuthorize("@ss.hasPermi('sys:user:addUser')")
    @RequestMapping("/addUser")
    public ResultUtil addUser(user user){
        userservice.insertUser(user);
        return ResultUtil.ok();
    }

    @PreAuthorize("@ss.hasPermi('sys:user:deleteUser')")
    @RequestMapping("/deleteUser")
    public ResultUtil deleteUser(HttpServletRequest request){
        String userId=request.getParameter("id");
        userservice.deleteUserByid(Integer.parseInt(userId));
        return ResultUtil.ok();
    }
    @PreAuthorize("@ss.hasPermi('sys:user:editUser')")
    @RequestMapping("/editUser")
    public ResultUtil editUser(HttpServletRequest request,Map<String,Object> map){
        user user;
        String userId=request.getParameter("id");
        user=userservice.findUserById(Integer.parseInt(userId));
        user.setTouxiag_url("http://localhost:8083/"+user.getTouxiag_url());
        map.put("user",user);
        return ResultUtil.ok().data(map);
    }
    @PreAuthorize("@ss.hasPermi('sys:user:rsetUserPs')")
    @RequestMapping("/rsetUserPs")
    public ResultUtil rsetUserPs(HttpServletRequest request, user user){
        String userId=request.getParameter("id");
        user=userservice.findUserById(Integer.parseInt(userId));
        user.setPassword("123456");
        userservice.updateUser(user);
        return ResultUtil.ok();
    }


    @RequestMapping("/currentUser")
    public ResultUtil currentUser(HttpServletRequest request, Map<String,Object> map){
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        user user=loginUser.getUser();
        user.setTouxiag_url("http://localhost:8083/"+user.getTouxiag_url());
        map.put("currentUser",user);
        return ResultUtil.ok().data(map);
    }


}
