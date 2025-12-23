package com.hz.voa.api;

import com.hz.voa.pojo.OrderVO;
import com.hz.voa.pojo.OrderRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 * @author rhb
 * @date 2025/12/23 18:30
 **/
public interface OrderApi {

    @PostMapping("order/create")
    OrderVO create(@RequestBody OrderRequest request);

    @PostMapping("order/try/create")
    OrderVO tryCreate(@RequestBody OrderRequest request);

}
