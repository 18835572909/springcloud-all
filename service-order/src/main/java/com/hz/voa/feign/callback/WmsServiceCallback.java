package com.hz.voa.feign.callback;

import com.hz.voa.feign.WmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 放弃hystrix
 * @author rhb
 * @date 2025/12/23 14:49
 **/
@Deprecated
@Slf4j
@Service
@SuppressWarnings("all")
public class WmsServiceCallback implements WmsService {

    private final static String SERVICE_NAME = "[wms]";

    @Override
    public void addRecord(String commodityCode, int count) {
        log.info("服务{}异常:{}", SERVICE_NAME, "deduct");
        throw new RuntimeException("服务异常");
    }

    @Override
    public void tryAddRecord(String commodityCode, int count) {
        log.info("服务{}异常:{}", SERVICE_NAME, "tryDeduct");
        throw new RuntimeException("服务异常");
    }

}
