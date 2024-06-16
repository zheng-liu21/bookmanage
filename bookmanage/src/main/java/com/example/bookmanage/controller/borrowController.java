package com.example.bookmanage.controller;


import com.example.bookmanage.entity.borrow;
import com.example.bookmanage.service.borrowService;
import com.example.bookmanage.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/borrow")
public class borrowController {
    @Autowired
    private borrowService borrowService;

    @PreAuthorize("@ss.hasPermi('sys:user:findAllborrow')")
    @RequestMapping("/findAllborrow")
    public ResultUtil findAllborrow(HttpServletRequest request, Map<String,Object> map){
        String query=request.getParameter("query");
        borrow queryborrow=new borrow();
        queryborrow.setReader_name(query);
        //截取List查询结果分页
        int pagenum= Integer.parseInt(request.getParameter("pagenum"));
        int pagesize= Integer.parseInt(request.getParameter("pagesize"));
        int count=0;
        List<borrow> borrowList=borrowService.findAllborrow(queryborrow);
        for(borrow borrow : borrowList){
            if(borrow.getStatus().equals("0"))
                borrow.setStatus_name("待还");
            else if(borrow.getStatus().equals("1"))
                borrow.setStatus_name("超期");
            else borrow.setStatus_name("已还");
        }
        if(borrowList!=null && borrowList.size()>0){
            count=borrowList.size();
            int fromIndex=(pagenum-1)*pagesize;
            int toIndex=pagenum*pagesize;
            if(toIndex>count)
                toIndex=count;
            List<borrow> pageBorrowList=borrowList.subList(fromIndex,toIndex);
            map.put("borrowList",pageBorrowList);
            map.put("pagenum",pagenum);
        }
        map.put("total",borrowList.size());
        return ResultUtil.ok().data(map);
    }

    @PreAuthorize("@ss.hasPermi('sys:user:addBorrow')")
    @RequestMapping("/addBorrow")
    public ResultUtil addBorrow(borrow borrow){
        borrowService.insertBorrow(borrow);
        return ResultUtil.ok();
    }

    @PreAuthorize("@ss.hasPermi('sys:user:editBorrow')")
    @RequestMapping("/editBorrow")
    public ResultUtil editBorrow(HttpServletRequest request, Map<String,Object> map){
        borrow borrow;
        String borrowId=request.getParameter("id");
        borrow=borrowService.findBorrowById(Integer.parseInt(borrowId));
        map.put("borrow",borrow);
        return ResultUtil.ok().data(map);
    }

    @PreAuthorize("@ss.hasPermi('sys:user:updateBorrow')")
    @RequestMapping("/updateBorrow")
    public ResultUtil updateBorrow(borrow borrow){
        borrowService.updateBorrow(borrow);
        return ResultUtil.ok();
    }

    @PreAuthorize("@ss.hasPermi('sys:user:deleteBorrow')")
    @RequestMapping("/deleteBorrow")
    public ResultUtil deleteBorrow(HttpServletRequest request){
        String borrowId=request.getParameter("id");
        borrowService.deleteBorrowrByid(Integer.parseInt(borrowId));
        return ResultUtil.ok();
    }

}
