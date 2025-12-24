package com.hz.voa;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 
 * @author rhb
 * @date 2025/12/23 11:37
 **/
@Slf4j
@EnableAsync
@EnableDiscoveryClient
@EnableFeignClients
@EnableAutoDataSourceProxy
@SpringBootApplication
public class OrderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Order Service Start !!!");
    }

    @Bean
    public CommandLineRunner checkSeataConfig(Environment env) {
        return args -> {
            System.out.println("=== Seata 配置检查 ===");
            System.out.println("seata.enabled: " + env.getProperty("seata.enabled"));
            System.out.println("seata.tx-service-group: " + env.getProperty("seata.tx-service-group"));
            System.out.println("seata.service.vgroup-mapping: " + env.getProperty("seata.service.vgroup-mapping.default_tx_group"));
        };
    }
}
