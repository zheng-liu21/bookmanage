package com.example.bookmanage.service;

import com.example.bookmanage.entity.readerType;
import com.example.bookmanage.mapper.ReaderTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class readerTypeService {
    @Autowired(required=false)
    public ReaderTypeMapper readerTypeMapper;
    public List<readerType> findAllreaderType(readerType readerType){
        return readerTypeMapper.findAllreaderType(readerType);
    }
    public int insertReaderType(readerType readerType){
        return readerTypeMapper.insertReaderType(readerType);
    }
    public readerType findReaderTypeById(int id){
        return readerTypeMapper.findReaderTypeById(id);
    }
    public int updateReaderType(readerType readerType){
        return readerTypeMapper.updateReaderType(readerType);
    }
    public int deleteReaderTypeByid(int id){
        return readerTypeMapper.deleteReaderTypeByid(id);
    }
}
