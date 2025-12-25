package com.hz.voa.config;

import feign.Request;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

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

    @Bean("option")
    public Request.Options option(){
        return new Request.Options(30, TimeUnit.SECONDS, 30, TimeUnit.SECONDS, true);
    }

} 