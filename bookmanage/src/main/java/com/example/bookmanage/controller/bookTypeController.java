package com.example.bookmanage.controller;

import com.example.bookmanage.entity.bookType;
import com.example.bookmanage.service.bookTypeService;
import com.example.bookmanage.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookType")
public class bookTypeController {
    @Autowired
    private bookTypeService bookTypeService;


    @PreAuthorize("@ss.hasPermi('sys:user:findAllbookType')")
    @RequestMapping("/findAllbookType")
    public ResultUtil findAllbookType(HttpServletRequest request, Map<String,Object> map){
        String query=request.getParameter("query");
        bookType querybookType=new bookType();
        querybookType.setType_name(query);
        //截取List查询结果分页
        String spagenum=request.getParameter("pagenum");
        int pagenum= Integer.parseInt(spagenum);
        int pagesize= Integer.parseInt(request.getParameter("pagesize"));
        int count=0;
        List<bookType> bookTypeList=bookTypeService.findAllbookType(querybookType);
        if(spagenum!=null&&bookTypeList!=null && bookTypeList.size()>0){
            count=bookTypeList.size();
            int fromIndex=(pagenum-1)*pagesize;
            int toIndex=pagenum*pagesize;
            if(toIndex>count)
                toIndex=count;
            List<bookType> pageBookList=bookTypeList.subList(fromIndex,toIndex);
            map.put("bookTypeList",pageBookList);
            map.put("pagenum",pagenum);
            map.put("total",bookTypeList.size());
            return ResultUtil.ok().data(map);
        }else {
            map.put("bookTypeList",bookTypeList);
            map.put("total",bookTypeList.size());
            return ResultUtil.ok().data(map);
        }

    }

    @PreAuthorize("@ss.hasPermi('sys:user:addBookType')")
    @RequestMapping("/addBookType")
    public ResultUtil addBookType(bookType bookType){
        bookTypeService.insertBookType(bookType);
        return ResultUtil.ok();
    }

    @PreAuthorize("@ss.hasPermi('sys:user:editBookType')")
    @RequestMapping("/editBookType")
    public ResultUtil editBookType(HttpServletRequest request, Map<String,Object> map){
        bookType bookType;
        String bookTypeId=request.getParameter("id");
        bookType=bookTypeService.findBookTypeById(Integer.parseInt(bookTypeId));
        map.put("bookType",bookType);
        return ResultUtil.ok().data(map);
    }

    @PreAuthorize("@ss.hasPermi('sys:user:updateBookType')")
    @RequestMapping("/updateBookType")
    public ResultUtil updateBookType(bookType bookType){
        bookTypeService.updateBookType(bookType);
        return ResultUtil.ok();
    }


    @RequestMapping("/deleteBookType")
    public ResultUtil deleteBookType(HttpServletRequest request){
        String bookTypeId=request.getParameter("id");
        bookTypeService.deleteBookTypeByid(Integer.parseInt(bookTypeId));
        return ResultUtil.ok();
    }

}
