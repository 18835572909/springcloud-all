package com.hz.voa.api;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @author rhb
 * @date 2025/12/23 11:42
**/
public interface WmsApi {

    @RequestMapping(value = "/wms/stock/addRecord",method = RequestMethod.GET)
    void addRecord(@RequestParam("commodityCode") String commodityCode, @RequestParam("count") int count);

    @RequestMapping(value = "/wms/stock/tryAddRecord",method = RequestMethod.GET)
    void tryAddRecord(@RequestParam("commodityCode") String commodityCode, @RequestParam("count") int count);

}
