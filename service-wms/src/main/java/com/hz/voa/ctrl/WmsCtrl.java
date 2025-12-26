package com.hz.voa.ctrl;

import com.hz.voa.api.WmsApi;
import com.hz.voa.service.AccountService;
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
    AccountService accountService;

    @Override
    public void deduct(String commodityCode, int count) {
        accountService.add(commodityCode, count);
    }

    @Override
    public void tryDeduct(String commodityCode, int count) {
        accountService.tryAdd(commodityCode, count);
    }

}
