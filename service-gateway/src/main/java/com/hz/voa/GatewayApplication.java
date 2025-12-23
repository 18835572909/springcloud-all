package com.hz.voa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * @author rhb
 * @date 2025/12/23 11:38
 **/
@Slf4j
@SpringBootApplication
public class GatewayApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Gateway Service Start !!!");
    }
}
