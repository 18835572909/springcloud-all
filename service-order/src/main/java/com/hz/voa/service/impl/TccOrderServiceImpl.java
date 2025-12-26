package com.hz.voa.service.impl;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hz.voa.feign.WmsService;
import com.hz.voa.mapper.OrderMapper;
import com.hz.voa.mapper.UndoLogMapper;
import com.hz.voa.pojo.OrderVO;
import com.hz.voa.service.TccOrderService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 
 * @author rhb
 * @date 2025/12/25 18:27
**/
@Slf4j
@Service
public class TccOrderServiceImpl implements TccOrderService {

    @Override
    public OrderVO tryCreateOrder(String userId, String commodityCode, int orderCount) {
        String xid = RootContext.getXID();
        log.info("tryOrder: {}", xid);

        return null;
    }

    @Override
    public OrderVO confirmOrder(BusinessActionContext bc) {
        String xid = bc.getXid();
        log.info("confirmOrder: {}", xid);
        return null;
    }

    @Override
    public OrderVO cancelOrder(BusinessActionContext bc) {
        String xid = bc.getXid();
        log.info("cancelOrder: {}", xid);

        return null;
    }
}
