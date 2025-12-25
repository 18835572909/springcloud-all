package com.hz.voa.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hz.voa.entity.Order;
import com.hz.voa.mapper.UndoLogMapper;
import com.hz.voa.pojo.OrderVO;
import com.hz.voa.feign.WmsService;
import com.hz.voa.mapper.OrderMapper;
import com.hz.voa.service.OrderService;
import com.hz.voa.transform.OrderMapping;
import io.seata.core.context.RootContext;
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

    @Resource
    UndoLogMapper undoLogMapper;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public OrderVO create(String userId, String commodityCode, int orderCount) {
        Order orderDomain = new Order();
        orderDomain.setUserId(userId);
        orderDomain.setItemId(commodityCode);
        orderDomain.setNum(orderCount);
        orderDomain.setPrice(new BigDecimal(50));
        orderDomain.setCouponPrice(new BigDecimal(2));
        orderDomain.setTotalPrice(orderDomain.getPrice().multiply(new BigDecimal(orderDomain.getNum())).subtract(orderDomain.getCouponPrice()));
        String orderNo = IdUtil.fastSimpleUUID();
        orderDomain.setOrderNo(orderNo);

        String xid = RootContext.getXID();

        log.info("[order插入前] xid:{}, undo_log.count:{} ", xid, undoLogCount());

        int count = orderMapper.insert(orderDomain);
        Order saveOrderDomain = null;
        if(count>0){
            saveOrderDomain = new LambdaQueryChainWrapper<>(this.baseMapper)
                    .eq(Order::getOrderNo, orderNo).one();
        }

        log.info("[order插入后] xid:{}, undo_log.count:{} ", xid, undoLogCount());

        // 这里设计异常，测试当前服务的回滚
        wmsService.deduct(commodityCode, orderCount);

        log.info("[wms执行后] xid:{}, undo_log.count:{} ", xid, undoLogCount());

        if (orderCount==8){
            throw new RuntimeException("主动异常：全局回滚");
        }

        return saveOrderDomain !=null ? OrderMapping.INSTANCE.toVo(saveOrderDomain) : null;
    }

    @TwoPhaseBusinessAction(name = "tryOrder", commitMethod = "confirmOrder", rollbackMethod = "cancelOrder")
    @Override
    public OrderVO tryCreate(String userId, String commodityCode, int orderCount) {
        Order orderDomain = new Order();
        orderDomain.setUserId(userId);
        orderDomain.setItemId(commodityCode);
        orderDomain.setNum(orderCount);
        orderDomain.setPrice(new BigDecimal(50));
        orderDomain.setCouponPrice(new BigDecimal(2));
        orderDomain.setTotalPrice(orderDomain.getPrice().multiply(new BigDecimal(orderDomain.getNum())).subtract(orderDomain.getCouponPrice()));
        String orderNo = IdUtil.fastSimpleUUID();
        orderDomain.setOrderNo(orderNo);

        int count = orderMapper.insert(orderDomain);
        Order saveOrderDomain = null;
        if(count>0){
            saveOrderDomain = new LambdaQueryChainWrapper<>(this.baseMapper)
                    .eq(Order::getOrderNo, orderNo).one();
        }

        wmsService.deduct(commodityCode, orderCount);

        return saveOrderDomain !=null ? OrderMapping.INSTANCE.toVo(saveOrderDomain) : null;
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

    private long undoLogCount(){
        return new LambdaQueryChainWrapper<>(undoLogMapper).count();
    }

}
