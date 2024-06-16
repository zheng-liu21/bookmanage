package com.example.bookmanage.mapper;

import com.example.bookmanage.entity.reader;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReaderMapper {
    public List<reader> findAllreader(reader reader);
    public int insertReader(reader reader);
    public int updateReader(reader reader);
    public reader findReaderById(int id);
    public int deleteReaderByid(int id);
}
