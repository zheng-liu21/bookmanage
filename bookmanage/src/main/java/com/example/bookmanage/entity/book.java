package com.example.bookmanage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class book {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishing_house() {
        return publishing_house;
    }

    public void setPublishing_house(String publishing_house) {
        this.publishing_house = publishing_house;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getPublishing_time() {
        return publishing_time;
    }

    public void setPublishing_time(Date publishing_time) {
        this.publishing_time = publishing_time;
    }

    public int getBook_type() {
        return book_type;
    }

    public void setBook_type(int book_type) {
        this.book_type = book_type;
    }


    private int id;
    private String book_name;
    private String author;
    private String publishing_house;
    private BigDecimal price;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishing_time;

    private int book_type;

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    private String type_name;

    public String getBookimage_url() {
        return bookimage_url;
    }

    public void setBookimage_url(String bookimage_url) {
        this.bookimage_url = bookimage_url;
    }

    private String bookimage_url;
}
