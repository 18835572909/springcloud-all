package com.hz.voa.service;

/**
 * 
 * @author rhb
 * @date 2025/12/23 11:40
 **/
public interface StockService {

    /**
     * 扣除存储数量
     */
    void deduct(String commodityCode, int count);

}
