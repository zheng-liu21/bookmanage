package com.example.bookmanage.controller;

import com.example.bookmanage.entity.reader;
import com.example.bookmanage.service.readerService;
import com.example.bookmanage.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reader")
public class readerController {
    @Autowired
    private readerService readerService;


    @PreAuthorize("@ss.hasPermi('sys:user:findAllreader')")
    @RequestMapping("/findAllreader")
    public ResultUtil findAllreader(HttpServletRequest request,Map<String,Object> map){
        String query=request.getParameter("query");
        reader queryreader=new reader();
        queryreader.setReader_name(query);
        //截取List查询结果分页
        int pagenum= Integer.parseInt(request.getParameter("pagenum"));
        int pagesize= Integer.parseInt(request.getParameter("pagesize"));
        int count=0;
        List<reader> readerList=readerService.findAllreader(queryreader);
        if(readerList!=null && readerList.size()>0){
            count=readerList.size();
            int fromIndex=(pagenum-1)*pagesize;
            int toIndex=pagenum*pagesize;
            if(toIndex>count)
                toIndex=count;
            List<reader> pageReaderList=readerList.subList(fromIndex,toIndex);
            map.put("readerList",pageReaderList);
            map.put("pagenum",pagenum);
        }
        map.put("total",readerList.size());
        return ResultUtil.ok().data(map);
    }
    @PreAuthorize("@ss.hasPermi('sys:user:addReader')")
    @RequestMapping("/addReader")
    public ResultUtil addReader(reader reader){
        readerService.insertReader(reader);
        return ResultUtil.ok();
    }
    @PreAuthorize("@ss.hasPermi('sys:user:editReader')")
    @RequestMapping("/editReader")
    public ResultUtil editReader(HttpServletRequest request, Map<String,Object> map){
        reader reader;
        String readerId=request.getParameter("id");
        reader=readerService.findReaderById(Integer.parseInt(readerId));
        map.put("reader",reader);
        return ResultUtil.ok().data(map);
    }

    @PreAuthorize("@ss.hasPermi('sys:user:updateReader')")
    @RequestMapping("/updateReader")
    public ResultUtil updateReader(reader reader){
        readerService.updateReader(reader);
        return ResultUtil.ok();
    }

    @PreAuthorize("@ss.hasPermi('sys:user:deleteReader')")
    @RequestMapping("/deleteReader")
    public ResultUtil deleteReader(HttpServletRequest request){
        String readerId=request.getParameter("id");
        readerService.deleteReaderByid(Integer.parseInt(readerId));
        return ResultUtil.ok();
    }

}
