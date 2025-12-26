package com.hz.voa.service;

/**
 * 
 * @author rhb
 * @date 2025/12/23 11:40
 **/
public interface AccountService {

    /**
     * 扣除存储数量
     */
    void add(String commodityCode, int count);

    /**
     * 扣除存储数量
     */
    void tryAdd(String commodityCode, int count);

}
