package com.lingxi.minimall.dto;

import java.math.BigDecimal;

public class ProductUpdateDTO {

    private String name;
    private BigDecimal price;
    private Integer stock;

    public ProductUpdateDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}