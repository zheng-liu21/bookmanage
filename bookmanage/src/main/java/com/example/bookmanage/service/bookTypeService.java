package com.example.bookmanage.service;

import com.example.bookmanage.entity.bookType;
import com.example.bookmanage.mapper.BookTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class bookTypeService {
    @Autowired(required=false)
    public BookTypeMapper bookTypeMapper;
    public List<bookType> findAllbookType(bookType bookType){
        return bookTypeMapper.findAllbookType(bookType);
    }

    public int insertBookType(bookType bookType){
        return bookTypeMapper.insertBookType(bookType);
    }

    public bookType findBookTypeById(int id){
        return bookTypeMapper.findBookTypeById(id);
    }

    public int updateBookType(bookType bookType){
        return bookTypeMapper.updateBookType(bookType);
    }

    public int deleteBookTypeByid(int id){
        return bookTypeMapper.deleteBookTypeByid(id);
    }
}
