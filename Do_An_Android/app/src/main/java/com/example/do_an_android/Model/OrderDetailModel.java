package com.example.do_an_android.Model;

import java.io.Serializable;

public class OrderDetailModel implements Serializable {
    private String codeOrder,nameProduct;
    private long price;
    private int quantity;
    private long total;

    public OrderDetailModel(String codeOrder, String nameProduct, long price, int quantity, long total) {
        this.codeOrder = codeOrder;
        this.nameProduct = nameProduct;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

    public String getCodeOrder() {
        return codeOrder;
    }

    public void setCodeOrder(String codeOrder) {
        this.codeOrder = codeOrder;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
