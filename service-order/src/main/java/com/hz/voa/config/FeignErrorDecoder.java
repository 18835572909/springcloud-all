package com.hz.voa.config;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author rhb
 * @date 2025/12/25 12:21
 **/
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        // 记录原始错误信息
        FeignErrorDecoder.ErrorBody errorBody = FeignErrorDecoder.ErrorBody.builder().methodKey(methodKey).body(JSONUtil.parseObj(response.body().toString())).build();
        log.error("Feign client调用失败: \n{}", JSONUtil.toJsonPrettyStr(errorBody));

        // 返回原始异常或自定义异常
        return new RuntimeException("Feign client调用失败: " + methodKey);
    }

    @Builder
    @Data
    public static class ErrorBody{
        private String methodKey;
        private JSONObject body;
    }
}
