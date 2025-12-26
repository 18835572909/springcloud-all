package com.hz.voa.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * 
 * @author rhb
 * @date 2025/12/25 19:13
 **/
@LocalTCC
public interface TccAccountService {

    @TwoPhaseBusinessAction(name = "tryDeduct", commitMethod = "commit", rollbackMethod = "cancel", useTCCFence = true)
    void tryDeduct(String commodityCode, int count);

    void commit(BusinessActionContext bc);

    void cancel(BusinessActionContext bc);

}
