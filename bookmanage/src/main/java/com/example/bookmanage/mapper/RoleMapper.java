package com.example.bookmanage.mapper;

import com.example.bookmanage.entity.role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色
 *
 * @Author zheng
 * @Date 2023/03/29 14:33:35
 * @Version 1.0
 */
@Mapper
public interface RoleMapper {
    public role findRoleByName(String role_name);
    public List<role> findAllrole(role role);
    public int updateRole(role role);
    public int insertRole(role role);
    public int deleteRoleByid(int id);
    public role findRoleById(int id);
}
