package com.example.bookmanage.mapper;

import com.example.bookmanage.entity.bookType;
import com.example.bookmanage.entity.readerType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookTypeMapper {

    public List<bookType> findAllbookType(bookType bookType);
    public int insertBookType(bookType bookType);
    public bookType findBookTypeById(int id);
    public int updateBookType(bookType bookType);
    public int deleteBookTypeByid(int id);
}
