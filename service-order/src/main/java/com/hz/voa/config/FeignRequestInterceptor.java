package com.hz.voa.config;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
import org.springframework.stereotype.Component;

/**
 * 
 * @author rhb
 * @date 2025/12/25 12:21
 **/
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        String xid = RootContext.getXID();
        if (StrUtil.isNotBlank(xid)) {
            template.header(RootContext.KEY_XID, xid);
        }
    }
}
