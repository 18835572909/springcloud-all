package com.hz.voa.service;

import com.hz.voa.pojo.OrderVO;

/**
 * 
 * @author rhb
 * @date 2025/12/23 11:39
**/
public interface OrderService {

    /**
     * 创建订单
     */
    OrderVO create(String userId, String commodityCode, int orderCount);

    OrderVO tryCreateOrder(String userId, String commodityCode, int orderCount);
}
