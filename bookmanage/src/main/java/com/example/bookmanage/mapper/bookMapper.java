package com.example.bookmanage.mapper;

import com.example.bookmanage.entity.book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface bookMapper {
    public List<book> findAllbook(book book);
    public int insertBook(book book);
    public int updateBook(book book);
    public book findBookById(int id);
    public book findBookByName(String bookname);
    public int deleteBookByid(int id);
}
