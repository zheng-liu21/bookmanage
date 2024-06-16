package com.example.bookmanage.controller;

import com.example.bookmanage.entity.book;
import com.example.bookmanage.service.bookService;
import com.example.bookmanage.utils.FileUploadUtils;
import com.example.bookmanage.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class bookController {
    @Autowired
    private bookService bookService;

    @PreAuthorize("@ss.hasPermi('sys:user:findAllbook')")
    @RequestMapping("/findAllbook")
    public ResultUtil findAllbook(HttpServletRequest request, Map<String,Object> map){
        String query=request.getParameter("query");
        book querybook=new book();
        querybook.setBook_name(query);
        //截取List查询结果分页
        int pagenum= Integer.parseInt(request.getParameter("pagenum"));
        int pagesize= Integer.parseInt(request.getParameter("pagesize"));
        int count=0;
        List<book> bookList=bookService.findAllbook(querybook);
        for(book book : bookList){
           book.setBookimage_url( "http://localhost:8083/"+book.getBookimage_url());
        }
        if(bookList!=null && bookList.size()>0){
            count=bookList.size();
            int fromIndex=(pagenum-1)*pagesize;
            int toIndex=pagenum*pagesize;
            if(toIndex>count)
                toIndex=count;
            List<book> pageBookList=bookList.subList(fromIndex,toIndex);
            map.put("bookList",pageBookList);
            map.put("pagenum",pagenum);
        }
        map.put("total",bookList.size());
        return ResultUtil.ok().data(map);
    }


    @PreAuthorize("@ss.hasPermi('sys:user:addBook')")
    @RequestMapping("/addBook")
    public ResultUtil addBook(book book){
        bookService.insertBook(book);
        return ResultUtil.ok();
    }


    @PreAuthorize("@ss.hasPermi('sys:user:editBook')")
    @RequestMapping("/editBook")
    public ResultUtil editBook(HttpServletRequest request, Map<String,Object> map){
        book book;
        String bookId=request.getParameter("id");
        book=bookService.findBookById(Integer.parseInt(bookId));
        book.setBookimage_url("http://localhost:8083/"+book.getBookimage_url());
        map.put("book",book);
        return ResultUtil.ok().data(map);
    }

    @PreAuthorize("@ss.hasPermi('sys:user:updateBook')")
    @RequestMapping("/updateBook")
    public ResultUtil updateBook(book book){
        bookService.updateBook(book);
        return ResultUtil.ok();
    }

    @PreAuthorize("@ss.hasPermi('sys:user:deleteBook')")
    @RequestMapping("/deleteBook")
    public ResultUtil deleteBorrow(HttpServletRequest request){
        String bookId=request.getParameter("id");
        bookService.deleteBookByid(Integer.parseInt(bookId));
        return ResultUtil.ok();
    }
}
