package com.example.do_an_android.Model;

import java.io.Serializable;

public class ProductModel implements Serializable {
   private String code,name;
   private long price;
   private int quantity;
   private long price_discounted;
   private String description,image,date_update,type_code;

    public ProductModel(String code, String name, long price, int quantity, long price_discounted, String description, String image, String date_update, String type_code) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.price_discounted = price_discounted;
        this.description = description;
        this.image = image;
        this.date_update = date_update;
        this.type_code = type_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice_discounted() {
        return price_discounted;
    }

    public void setPrice_discounted(long price_discounted) {
        this.price_discounted = price_discounted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate_update() {
        return date_update;
    }

    public void setDate_update(String date_update) {
        this.date_update = date_update;
    }

    public String getType_code() {
        return type_code;
    }

    public void setType_code(String type_code) {
        this.type_code = type_code;
    }
}
