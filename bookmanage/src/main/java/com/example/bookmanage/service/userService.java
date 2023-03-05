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
    public List<user> findAlluser(){return usermapper.findAlluser();};
}
