package com.example.bookmanage.controller;

import com.example.bookmanage.entity.readerType;
import com.example.bookmanage.service.readerTypeService;
import com.example.bookmanage.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/readerType")
public class readerTypeController {
    @Autowired
    private readerTypeService readerTypeService;

    @PreAuthorize("@ss.hasPermi('sys:user:findAllreaderType')")
    @RequestMapping("/findAllreaderType")
    public ResultUtil findAllreaderType(HttpServletRequest request, Map<String,Object> map){
        String query=request.getParameter("query");
        readerType queryreaderType=new readerType();
        queryreaderType.setType_name(query);
        //截取List查询结果分页
        String spagenum=request.getParameter("pagenum");
        int pagenum= Integer.parseInt(spagenum);
        int pagesize= Integer.parseInt(request.getParameter("pagesize"));
        int count=0;
        List<readerType> readerTypeList=readerTypeService.findAllreaderType(queryreaderType);
        if(spagenum!=null&&readerTypeList!=null && readerTypeList.size()>0){
            count=readerTypeList.size();
            int fromIndex=(pagenum-1)*pagesize;
            int toIndex=pagenum*pagesize;
            if(toIndex>count)
                toIndex=count;
            List<readerType> pageReaderList=readerTypeList.subList(fromIndex,toIndex);
            map.put("readerTypeList",pageReaderList);
            map.put("pagenum",pagenum);
            map.put("total",readerTypeList.size());
            return ResultUtil.ok().data(map);
        }else {
            map.put("readerTypeList",readerTypeList);
            map.put("total",readerTypeList.size());
            return ResultUtil.ok().data(map);
        }

    }

    @PreAuthorize("@ss.hasPermi('sys:user:addReaderType')")
    @RequestMapping("/addReaderType")
    public ResultUtil addReaderType(readerType readerType){
        readerTypeService.insertReaderType(readerType);
        return ResultUtil.ok();
    }


    @PreAuthorize("@ss.hasPermi('sys:user:editReaderType')")
    @RequestMapping("/editReaderType")
    public ResultUtil editReaderType(HttpServletRequest request, Map<String,Object> map){
        readerType readerType;
        String readerTypeId=request.getParameter("id");
        readerType=readerTypeService.findReaderTypeById(Integer.parseInt(readerTypeId));
        map.put("readerType",readerType);
        return ResultUtil.ok().data(map);
    }

    @PreAuthorize("@ss.hasPermi('sys:user:updateReaderType')")
    @RequestMapping("/updateReaderType")
    public ResultUtil updateReaderType(readerType readerType){
        readerTypeService.updateReaderType(readerType);
        return ResultUtil.ok();
    }

    @PreAuthorize("@ss.hasPermi('sys:user:deleteReaderType')")
    @RequestMapping("/deleteReaderType")
    public ResultUtil deleteReaderType(HttpServletRequest request){
        String bookTypeId=request.getParameter("id");
        readerTypeService.deleteReaderTypeByid(Integer.parseInt(bookTypeId));
        return ResultUtil.ok();
    }
}
