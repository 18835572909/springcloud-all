package com.hz.voa.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 
 * @author rhb
 * @date 2025/12/23 11:41
 **/
@Data
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

}
