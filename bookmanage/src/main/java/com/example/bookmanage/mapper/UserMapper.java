package com.example.bookmanage.mapper;

import com.example.bookmanage.entity.user;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    public user findUserByName(String user_name);
    public List<user> findAlluser(user user);
    public int updateUser(user user);
    public int insertUser(user user);
    public int deleteUserByid(int id);
    public user findUserById(int id);
}
