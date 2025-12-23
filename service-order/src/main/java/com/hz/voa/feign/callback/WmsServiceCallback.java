package com.hz.voa.feign.callback;

import com.hz.voa.feign.WmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 
 * @author rhb
 * @date 2025/12/23 14:49
 **/
@Slf4j
@Service
public class WmsServiceCallback implements WmsService {

    private final static String SERVICE_NAME = "[wms]";

    @Override
    public void deduct(String commodityCode, int count) {
        log.info("服务{}异常:{}", SERVICE_NAME, "deduct");
        throw new RuntimeException("服务异常");
    }

}
