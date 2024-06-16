package com.example.bookmanage.service;

import com.example.bookmanage.entity.menu;
import com.example.bookmanage.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class menuService {
    @Autowired(required=false)
    public MenuMapper menuMapper;
    public List<menu> findAllmenu(menu menu){
        return menuMapper.findAllmenu(menu);
    }
    public List<menu> findAllmenuPerm(menu menu){
        return menuMapper.findAllmenuPerm(menu);
    }
    public Set<String> findMenuByUserId(int id){
        return menuMapper.findMenuByUserId(id);
    }
    public Set<String> findMenuPermByUserId(int id){
        return menuMapper.findMenuPermByUserId(id);
    }
    public menu findMenuById(int id){
        return menuMapper.findMenuById(id);
    }
    public menu findMenuByName(String menu_name){
        return menuMapper.findMenuByName(menu_name);
    }

    public int updateMenu(menu menu){
        return menuMapper.updateMenu(menu);
    }
    public int insertMenu(menu menu){
        return menuMapper.insertMenu(menu);
    }
    public int deleteMenuByid(int id){
        return menuMapper.deleteMenuByid(id);
    }
}
