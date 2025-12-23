package com.hz.voa.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 
 * @author rhb
 * @date 2025/12/23 11:41
 **/
@Data
public class OrderVO {

    private Long id;

    private String userId;

    private String orderNo;

    private String itemId;

    private BigDecimal price;

    private BigDecimal totalPrice;

    private BigDecimal couponPrice;

    private Integer num;

}
