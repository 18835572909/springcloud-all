package com.hz.voa.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hz.voa.entity.AccountRecord;
import com.hz.voa.mapper.AccountMapper;
import com.hz.voa.mapper.UndoLogMapper;
import com.hz.voa.service.AccountService;
import com.hz.voa.service.TccAccountService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 
 * @author rhb
 * @date 2025/12/23 11:40
 **/
@Slf4j
@Service
@SuppressWarnings("all")
public class AccountServiceImpl implements AccountService {

    @Resource
    AccountMapper accountMapper;

    @Resource
    UndoLogMapper undoLogMapper;

    @Resource
    TccAccountService tccAccountService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(String commodityCode, int count) {
        String xid = RootContext.getXID();
        boolean inGlobalTransaction = RootContext.inGlobalTransaction();
        log.info("[order插入前] xid:{}, inGlobalTransaction:{}, undo_log.count:{} ", xid, inGlobalTransaction, undoLogCount());
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
        log.info("[order插入后] xid:{}, undo_log.count:{} ", xid, undoLogCount());
    }

    @Override
    public void tryAdd(String commodityCode, int count) {
        String xid = RootContext.getXID();
        boolean inGlobalTransaction = RootContext.inGlobalTransaction();
        log.info("[order插入前] xid:{}, inGlobalTransaction:{}, undo_log.count:{} ", xid, inGlobalTransaction, undoLogCount());
        tccAccountService.tryDeduct(commodityCode, count);
        log.info("[order插入后] xid:{}, undo_log.count:{} ", xid, undoLogCount());
    }

    private long undoLogCount(){
        return new LambdaQueryChainWrapper<>(undoLogMapper).count();
    }

}
