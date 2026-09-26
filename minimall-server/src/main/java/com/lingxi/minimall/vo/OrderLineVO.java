package com.lingxi.minimall.vo;

import java.math.BigDecimal;

/** JOIN 查询得到的订单明细：历史商品快照加上当前分类信息。 */
public class OrderLineVO {
    private Long id;
    private Long productId;
    private String productName;
    private String categoryName;
    private Integer currentProductStatus;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal subtotal;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public Integer getCurrentProductStatus() { return currentProductStatus; }
    public void setCurrentProductStatus(Integer currentProductStatus) { this.currentProductStatus = currentProductStatus; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
