package com.example.bookmanage.entity;

/**
 * 角色实体
 *
 * @Author zheng
 * @Date 2023/03/29 14:29:18
 * @Version 1.0
 */
public class role {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    private String role_name;
}
