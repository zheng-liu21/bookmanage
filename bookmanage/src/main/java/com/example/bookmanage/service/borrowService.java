package com.example.bookmanage.service;

import com.example.bookmanage.entity.borrow;
import com.example.bookmanage.mapper.BorrowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class borrowService {
    @Autowired(required=false)
    public BorrowMapper borrowMapper;
    public List<borrow> findAllborrow(borrow borrow){
        return borrowMapper.findAllborrow(borrow);
    }
    public int insertBorrow(borrow borrow){
        return borrowMapper.insertBorrow(borrow);
    }
    public int updateBorrow(borrow borrow){
        return borrowMapper.updateBorrow(borrow);
    }
    public borrow findBorrowById(int id){
        return borrowMapper.findBorrowById(id);
    }
    public borrow findBorrowByreaderName(String readerName){
        return borrowMapper.findBorrowByreaderName(readerName);
    }
    public int deleteBorrowrByid(int id){
        return borrowMapper.deleteBorrowrByid(id);
    }

}
