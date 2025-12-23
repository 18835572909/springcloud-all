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

    @RequestMapping(value = "/wms/item/getDetail",method = RequestMethod.GET)
    void deduct(@RequestParam("commodityCode") String commodityCode, @RequestParam("count") int count);

}
