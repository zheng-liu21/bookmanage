package com.example.bookmanage.service;

import com.example.bookmanage.entity.book;
import com.example.bookmanage.mapper.bookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class bookService {
    @Autowired(required=false)
    public bookMapper bookMapper;
    public List<book> findAllbook(book book){
        return bookMapper.findAllbook(book);
    }
    public int insertBook(book book){
        return bookMapper.insertBook(book);
    }
    public int updateBook(book book){
//        String boo_url=book.getBookimage_url();
//        String url=boo_url.substring(44);
//        book.setBookimage_url(url);
        return bookMapper.updateBook(book);
    }
    public book findBookById(int id){
        return bookMapper.findBookById(id);
    }
    public book findBookByName(String bookname){
        return bookMapper.findBookByName(bookname);
    }
    public int deleteBookByid(int id){
        return bookMapper.deleteBookByid(id);
    }
}
