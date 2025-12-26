package com.hz.voa.ctrl;

import com.hz.voa.api.OrderApi;
import com.hz.voa.pojo.OrderVO;
import com.hz.voa.pojo.CreateOrderRequest;
import com.hz.voa.service.OrderService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 
 * @author rhb
 * @date 2025/12/23 18:35
 **/
@RestController
public class OrderCtrl implements OrderApi {

    @Resource
    OrderService orderService;

    @Override
    public OrderVO create(CreateOrderRequest request) {
        return orderService.create(request.getUserId(), request.getCommodityCode(), request.getOrderCount());
    }

    @Override
    public OrderVO tryCreate(CreateOrderRequest request) {
        return orderService.tryCreateOrder(request.getUserId(), request.getCommodityCode(), request.getOrderCount());
    }

}
