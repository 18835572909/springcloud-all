package com.hz.voa.config;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import feign.Response;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rhb
 */
@Slf4j
@Configuration
public class FeignConfig {
    
    @Bean("feignRetryer")
    public Retryer feignRetryer() {
        // 重试间隔为100ms，最大重试时间为1s，最大重试次数为3次
        return new Retryer.Default(100, 100, 3);
    }

    @Bean("errorDecoder")
    public ErrorDecoder errorDecoder(){
        log.info("FeignErrorDecoder 初始化成功！");
        return new FeignErrorDecoder();
    }

    @Slf4j
    public static class FeignErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            // 记录原始错误信息
            ErrorBody errorBody = ErrorBody.builder().methodKey(methodKey).body(JSONUtil.parseObj(response.body().toString())).build();
            log.error("Feign client调用失败: \n{}", JSONUtil.toJsonPrettyStr(errorBody));

            // 返回原始异常或自定义异常
            return new RuntimeException("Feign client调用失败: " + methodKey);
        }
    }

    @Builder
    @Data
    public static class ErrorBody{
        private String methodKey;
        private JSONObject body;
    }
} 