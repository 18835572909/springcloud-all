package com.hz.voa.feign;


import com.hz.voa.api.WmsApi;
import com.hz.voa.config.FeignConfig;
import com.hz.voa.feign.callback.WmsServiceCallback;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 
 * @author rhb
 * @date 2025/12/23 14:46
**/

@FeignClient(value = "service-wms",fallback = WmsServiceCallback.class,configuration = FeignConfig.class)
public interface WmsService extends WmsApi {

}
