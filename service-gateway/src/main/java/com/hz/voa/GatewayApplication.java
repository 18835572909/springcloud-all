package com.hz.voa;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        // 定义 API 分组
        //Set<ApiDefinition> definitions = new HashSet<>();
        //ApiDefinition api1 = new ApiDefinition("order-api")
        //        .setPredicateItems(new HashSet<ApiPredicateItem>() {{
        //            // 可以匹配多个路径
        //            add(new ApiPathPredicateItem().setPattern("/order/create/**"));
        //            add(new ApiPathPredicateItem().setPattern("/order/mock/**"));
        //        }});
        //ApiDefinition api2 = new ApiDefinition("wms-api")
        //        .setPredicateItems(new HashSet<ApiPredicateItem>() {{
        //            add(new ApiPathPredicateItem().setPattern("/wms/**"));
        //        }});
        //definitions.add(api1);
        //definitions.add(api2);
        //GatewayApiDefinitionManager.loadApiDefinitions(definitions);

        // 配置流控
        //Set<GatewayFlowRule> rules = new HashSet<>();
        //rules.add(new GatewayFlowRule("order-service")
        //                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID)
        //                .setCount(1)
        //                .setIntervalSec(1));
        //rules.add(new GatewayFlowRule("wms-service")
        //                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID)
        //                .setCount(1)
        //                .setIntervalSec(1));
        //GatewayRuleManager.loadRules(rules);

        log.info("Gateway Service Start !!!");
    }
}
