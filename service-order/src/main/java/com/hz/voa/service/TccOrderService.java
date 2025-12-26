package com.hz.voa.service;


import com.hz.voa.pojo.OrderVO;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * 
 * @author rhb
 * @date 2025/12/25 18:27
**/
@LocalTCC
public interface TccOrderService {

    /**
     * TCC
     */
    @TwoPhaseBusinessAction(name = "tryCreateOrder", commitMethod = "confirmOrder", rollbackMethod = "cancelOrder", useTCCFence = true)
    OrderVO tryCreateOrder(String userId, String commodityCode, int orderCount);

    OrderVO confirmOrder(BusinessActionContext context);

    OrderVO cancelOrder(BusinessActionContext context);

}
