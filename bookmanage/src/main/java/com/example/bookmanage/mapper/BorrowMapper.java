package com.example.bookmanage.mapper;

import com.example.bookmanage.entity.borrow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BorrowMapper {
    public List<borrow> findAllborrow(borrow borrow);
    public int insertBorrow(borrow borrow);
    public int updateBorrow(borrow borrow);
    public borrow findBorrowById(int id);
    public borrow findBorrowByreaderName(String readerName);
    public int deleteBorrowrByid(int id);
}
