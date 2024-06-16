package com.example.bookmanage.service;

import com.example.bookmanage.entity.reader;
import com.example.bookmanage.mapper.ReaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class readerService {
    @Autowired(required=false)
    public ReaderMapper readerMapper;
    public List<reader> findAllreader(reader reader){
        return readerMapper.findAllreader(reader);
    }
    public int insertReader(reader reader){
        reader.setBorrow_number(0);
        reader.setRegistration_date(new Date());
        return readerMapper.insertReader(reader);
    }
    public int updateReader(reader reader){
        return readerMapper.updateReader(reader);
    }
    public reader findReaderById(int id){
        return readerMapper.findReaderById(id);
    }

    public int deleteReaderByid(int id){
        return readerMapper.deleteReaderByid(id);
    }
}
