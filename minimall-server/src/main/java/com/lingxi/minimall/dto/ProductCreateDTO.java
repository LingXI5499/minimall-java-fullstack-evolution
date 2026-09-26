package com.lingxi.minimall.dto;

import java.math.BigDecimal;

public class ProductCreateDTO {
    private String name;
    private BigDecimal price;
    private Integer stock;
    public ProductCreateDTO(){}

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }
}
