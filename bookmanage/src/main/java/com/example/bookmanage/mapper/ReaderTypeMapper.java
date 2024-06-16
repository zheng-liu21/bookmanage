package com.example.bookmanage.mapper;

import com.example.bookmanage.entity.bookType;
import com.example.bookmanage.entity.readerType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReaderTypeMapper {
    public List<readerType> findAllreaderType(readerType readerType);
    public int insertReaderType(readerType readerType);
    public readerType findReaderTypeById(int id);
    public int updateReaderType(readerType readerType);
    public int deleteReaderTypeByid(int id);
}
