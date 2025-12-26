package com.hz.voa.service.impl;

import com.hz.voa.entity.AccountRecord;
import com.hz.voa.mapper.AccountMapper;
import com.hz.voa.service.TccAccountService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 
 * @author rhb
 * @date 2025/12/25 19:17
 **/
@Slf4j
@Service
@SuppressWarnings("all")
public class TccAccountServiceImpl implements TccAccountService {

    @Resource
    AccountMapper accountMapper;

    @Override
    public void tryDeduct(String commodityCode, int count) {
        String xid = RootContext.getXID();
        boolean inGlobalTransaction = RootContext.inGlobalTransaction();
        log.info("[order插入前] xid:{}, inGlobalTransaction:{}", xid, inGlobalTransaction);
        // 库存扣减占用，使用中间态标识
        if(count!=10){
            AccountRecord stock = new AccountRecord();
            stock.setStatus("decr");
            stock.setCurrentNum(count);
            stock.setTotal(10);
            stock.setItemId(commodityCode);
            accountMapper.insert(stock);
            log.info("库存扣减成功");
        }else {
            throw new RuntimeException("主动抛出！！！");
        }
        log.info("[order插入后] xid:{}", xid);
    }

    @Override
    public void commit(BusinessActionContext bc) {
        String xid = bc.getXid();
        log.info("[commit] xid:{}", xid);
    }

    @Override
    public void cancel(BusinessActionContext bc) {
        String xid = bc.getXid();
        log.info("[cancel] xid:{}", xid);
    }

}
