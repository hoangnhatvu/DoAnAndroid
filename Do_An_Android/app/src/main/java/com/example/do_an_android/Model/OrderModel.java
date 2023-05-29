package com.example.do_an_android.Model;

import java.io.Serializable;

public class OrderModel implements Serializable {
    private String code,username;
    private long total;
    private String createDate;

    public OrderModel(String code, String username, long total, String createDate) {
        this.code = code;
        this.username = username;
        this.total = total;
        this.createDate = createDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
