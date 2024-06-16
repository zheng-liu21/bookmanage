package com.example.bookmanage.mapper;

import com.example.bookmanage.entity.menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface MenuMapper {
    public List<menu> findAllmenu(menu menu);
    public List<menu> findAllmenuPerm(menu menu);
    public Set<String> findMenuByUserId(int id);
    public Set<String> findMenuPermByUserId(int id);
    public menu findMenuById(int id);
    public menu findMenuByName(String menu_name);
    public int updateMenu(menu menu);
    public int insertMenu(menu menu);
    public int deleteMenuByid(int id);
}
