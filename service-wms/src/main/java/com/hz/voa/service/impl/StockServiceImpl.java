package com.hz.voa.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hz.voa.entity.Stock;
import com.hz.voa.mapper.StockMapper;
import com.hz.voa.mapper.UndoLogMapper;
import com.hz.voa.service.StockService;
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
public class StockServiceImpl implements StockService {

    @Resource
    StockMapper stockMapper;

    @Resource
    UndoLogMapper undoLogMapper;

    //@Transactional(rollbackFor = Exception.class)
    @Override
    public void deduct(String commodityCode, int count) {
        String xid = RootContext.getXID();
        boolean inGlobalTransaction = RootContext.inGlobalTransaction();
        log.info("[order插入前] xid:{}, inGlobalTransaction:{}, undo_log.count:{} ", xid, inGlobalTransaction, undoLogCount());
        // 库存扣减占用，使用中间态标识
        if(count!=10){
            Stock stock = new Stock();
            stock.setStatus("PREPARE");
            stock.setCurrentNum(count);
            stock.setTotal(10);
            stock.setItemId(commodityCode);
            stockMapper.insert(stock);
            log.info("库存扣减成功");
        }else {
            throw new RuntimeException("主动抛出！！！");
        }
        log.info("[order插入后] xid:{}, undo_log.count:{} ", xid, undoLogCount());
    }

    private long undoLogCount(){
        return new LambdaQueryChainWrapper<>(undoLogMapper).count();
    }

}
