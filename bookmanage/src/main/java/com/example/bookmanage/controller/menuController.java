//package com.example.bookmanage.controller;
//
//import com.example.bookmanage.entity.menu;
//import com.example.bookmanage.service.menuService;
//import com.example.bookmanage.utils.ResultUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/menu")
//public class menuController {
//    @Autowired
//    private menuService menuService;
//    @RequestMapping("/findAllMenu")
//    public ResultUtil findAllUser(HttpServletRequest request, Map<String,Object> map){
//        List<menu> menuList=menuService.findAllmenu(menu);
//        map.put("menuList",menuList);
//        return ResultUtil.ok().data(map);
//    }
//}
