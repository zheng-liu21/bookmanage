package com.example.bookmanage.entity;

public class user {


    public static boolean isAdmin(int userId) {
        return 5 == userId;
    }
    public boolean isAdmin() {
        return isAdmin(this.id);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    private int id;
    private String user_name;
    private String password;
    private String phone;
    private String id_number;

    public String getTouxiag_url() {
        return touxiag_url;
    }

    public void setTouxiag_url(String touxiag_url) {
        this.touxiag_url = touxiag_url;
    }

    private String touxiag_url;

    public Boolean getState_bool() {
        return state_bool;
    }

    public void setState_bool(Boolean state_bool) {
        this.state_bool = state_bool;
    }

    private Boolean state_bool;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    //    用户状态；1为正常，0不正常
    private String state;
}
