package com.lingxi.minimall.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** 修改商品的请求数据；商品 ID 由 URL 提供，不相信请求体中的 ID。 */
public class ProductUpdateDTO {

    @NotBlank(message = "商品名称不能为空")
    private String name;
    @NotNull(message = "价格不能为空") @DecimalMin(value = "0.0", message = "价格不能为负")
    private BigDecimal price;
    @NotNull(message = "库存不能为空") @Min(value = 0, message = "库存不能为负")
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
