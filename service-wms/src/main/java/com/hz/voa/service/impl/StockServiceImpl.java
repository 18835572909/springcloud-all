package com.hz.voa.service.impl;

import com.hz.voa.entity.Stock;
import com.hz.voa.mapper.StockMapper;
import com.hz.voa.service.StockService;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deduct(String commodityCode, int count) {
        // 库存扣减占用，使用中间态标识
        if(count!=10){
            log.info("库存扣减成功");
            Stock stock = new Stock();
            stock.setStatus("");
            stock.setCurrentNum(count);
            stock.setTotal(10);
            stock.setItemId(commodityCode);
            stockMapper.insert(stock);
        }else {
            throw new RuntimeException("主动抛出！！！");
        }
    }

}
