package com.example.bookmanage.service;

import com.example.bookmanage.entity.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @Author zheng
 * @Date 2023/03/29 15:43:54
 * @Version 1.0
 */
@Component
public class SysPermissionService {

    @Autowired
    private roleService roleService;

    @Autowired
    private menuService menuService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
//    public Set<String> getRolePermission(user user) {
//        Set<String> roles = new HashSet<String>();
//        // 管理员拥有所有权限
//        if (user.isAdmin()) {
//            roles.add("admin");
//        } else {
//            roles.addAll(roleService.(user.getUserId()));
//        }
//        return roles;
//    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(user user) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else {
            perms.addAll(menuService.findMenuPermByUserId(user.getId()));
        }
        return perms;
    }
}

