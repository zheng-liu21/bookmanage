package com.example.bookmanage.service;

import com.example.bookmanage.entity.user;
import com.example.bookmanage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class userService {
    @Autowired(required=false)
    public UserMapper usermapper;


    public user findUserByName(String user_name){return usermapper.findUserByName(user_name);}
    public List<user> findAlluser(user user){return usermapper.findAlluser(user);};
    public int updateUser(user user){
        return usermapper.updateUser(user);
    }
    public int insertUser(user user){
        user.setState("1");
        return usermapper.insertUser(user);
    }
    public int deleteUserByid(int id){
        return usermapper.deleteUserByid(id);
    }
    public user findUserById(int id){
        return usermapper.findUserById(id);
    }
}
