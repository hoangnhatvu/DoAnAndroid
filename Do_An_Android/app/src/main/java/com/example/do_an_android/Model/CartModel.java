package com.example.do_an_android.Model;

import java.io.Serializable;

public class CartModel implements Serializable {
  private ProductModel productModel;
  private  int quantity,quantityRemain;

    public CartModel(ProductModel productModel, int quantity, int quantityRemain) {
        this.productModel = productModel;
        this.quantity = quantity;
        this.quantityRemain = quantityRemain;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantityRemain() {
        return quantityRemain;
    }

    public void setQuantityRemain(int quantityRemain) {
        this.quantityRemain = quantityRemain;
    }
}