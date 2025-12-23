package com.hz.voa.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hz.voa.entity.Order;
import com.hz.voa.pojo.OrderVO;
import com.hz.voa.feign.WmsService;
import com.hz.voa.mapper.OrderMapper;
import com.hz.voa.service.OrderService;
import com.hz.voa.transform.OrderMapping;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 
 * @author rhb
 * @date 2025/12/23 11:39
 **/
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    WmsService wmsService;

    @Resource
    OrderMapper orderMapper;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public OrderVO create(String userId, String commodityCode, int orderCount) {
        Order order = new Order();
        order.setUserId(userId);
        order.setItemId(commodityCode);
        order.setNum(orderCount);
        order.setPrice(new BigDecimal(50));
        order.setCouponPrice(new BigDecimal(2));
        order.setTotalPrice(order.getPrice().multiply(new BigDecimal(order.getNum())).subtract(order.getCouponPrice()));
        String orderNo = IdUtil.fastSimpleUUID();
        order.setOrderNo(orderNo);

        int count = orderMapper.insert(order);
        Order saveOrder = null;
        if(count>0){
            saveOrder = new LambdaQueryChainWrapper<>(this.baseMapper)
                    .eq(Order::getOrderNo, orderNo).one();
        }

        wmsService.deduct(commodityCode, orderCount);

        return saveOrder!=null ? OrderMapping.INSTANCE.toVo(saveOrder) : null;
    }

    @TwoPhaseBusinessAction(name = "tryOrder", commitMethod = "confirmOrder", rollbackMethod = "cancelOrder")
    @Override
    public OrderVO tryCreate(String userId, String commodityCode, int orderCount) {
        Order order = new Order();
        order.setUserId(userId);
        order.setItemId(commodityCode);
        order.setNum(orderCount);
        order.setPrice(new BigDecimal(50));
        order.setCouponPrice(new BigDecimal(2));
        order.setTotalPrice(order.getPrice().multiply(new BigDecimal(order.getNum())).subtract(order.getCouponPrice()));
        String orderNo = IdUtil.fastSimpleUUID();
        order.setOrderNo(orderNo);

        int count = orderMapper.insert(order);
        Order saveOrder = null;
        if(count>0){
            saveOrder = new LambdaQueryChainWrapper<>(this.baseMapper)
                    .eq(Order::getOrderNo, orderNo).one();
        }

        wmsService.deduct(commodityCode, orderCount);

        return saveOrder!=null ? OrderMapping.INSTANCE.toVo(saveOrder) : null;
    }

    @Override
    public OrderVO confirmOrder(String userId, String commodityCode, int orderCount) {
        log.info("confirmOrder ...");
        return null;
    }

    @Override
    public OrderVO cancelOrder(String userId, String commodityCode, int orderCount) {
        log.info("cancelOrder ...");
        return null;
    }
}
