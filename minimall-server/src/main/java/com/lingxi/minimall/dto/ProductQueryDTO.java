package com.lingxi.minimall.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class ProductQueryDTO {

    @Min(value = 1, message = "page 必须大于等于 1")
    @Max(value = 1000000, message = "page 过大")
    private Integer page = 1;

    @Min(value = 1, message = "pageSize 必须大于等于 1")
    @Max(value = 50, message = "pageSize 不能超过 50")
    private Integer pageSize = 5;

    private String name;

    private Integer status;

    @DecimalMin(value = "0.0", message = "最低价格不能为负")
    private BigDecimal minPrice;

    @DecimalMin(value = "0.0", message = "最高价格不能为负")
    private BigDecimal maxPrice;

    private Integer offset;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
