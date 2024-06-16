package com.example.bookmanage.service;

import com.example.bookmanage.entity.role;
import com.example.bookmanage.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色
 *
 * @Author zheng
 * @Date 2023/03/29 14:34:39
 * @Version 1.0
 */
@Service
public class roleService {
    @Autowired(required=false)
    public RoleMapper roleMapper;


    public role findRoleByName(String role_name){
        return roleMapper.findRoleByName(role_name);
    }
    public List<role> findAllrole(role role){
        return roleMapper.findAllrole(role);
    }
    public int updateRole(role role){
        return roleMapper.updateRole(role);
    }
    public int insertRole(role role){
        return roleMapper.insertRole(role);
    }
    public int deleteRoleByid(int id){
        return roleMapper.deleteRoleByid(id);
    }
    public role findRoleById(int id){
        return roleMapper.findRoleById(id);
    }
}
