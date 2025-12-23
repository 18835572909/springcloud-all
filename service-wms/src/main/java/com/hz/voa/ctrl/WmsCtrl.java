package com.hz.voa.ctrl;

import com.hz.voa.api.WmsApi;
import com.hz.voa.service.StockService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 
 * @author rhb
 * @date 2025/12/23 11:44
 **/
@RestController
public class WmsCtrl implements WmsApi {

    @Resource
    StockService stockService;

    @Override
    public void deduct(String commodityCode, int count) {
        stockService.deduct(commodityCode, count);
    }

}
