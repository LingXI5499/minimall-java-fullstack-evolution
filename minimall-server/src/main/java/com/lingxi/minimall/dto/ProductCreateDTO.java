package com.lingxi.minimall.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductCreateDTO {
    @NotBlank(message = "商品名称不能为空")
    private String name;
    @NotNull(message = "价格不能为空") @DecimalMin(value = "0.0", message = "价格不能为负")
    private BigDecimal price;
    @NotNull(message = "库存不能为空") @Min(value = 0, message = "库存不能为负")
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
