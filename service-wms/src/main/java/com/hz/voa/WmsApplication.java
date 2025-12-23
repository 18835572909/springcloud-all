package com.hz.voa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * 
 * @author rhb
 * @date 2025/12/23 11:38
 **/
@Slf4j
@SpringBootApplication
public class WmsApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WmsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Wms Service Start !!!");
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
