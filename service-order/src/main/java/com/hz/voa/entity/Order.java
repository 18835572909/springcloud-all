package com.hz.voa.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

/**
 * 
 * @author rhb
 * @date 2025/12/23 11:41
 **/
@TableName("t_order")
public class Order {

    private Long id;

    private String userId;

    private String orderNo;

    private String itemId;

    private BigDecimal price;

    private BigDecimal totalPrice;

    private BigDecimal couponPrice;

    private Integer num;

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getItemId() {
        return itemId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    public Integer getNum() {
        return num;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

}
