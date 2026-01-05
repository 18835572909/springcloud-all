package com.hz.voa.ctrl;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author rhb
 * @date 2026/1/5 15:48
 **/
@RestController
@RequestMapping("/gw")
public class GatewayCtrl {

    @Value("${spring.cloud.sentinel.transport.dashboard:}")
    private String dashboard;

    @Value("${spring.cloud.sentinel.transport.port:}")
    private String transportPort;

    @GetMapping("/status")
    public Map<String, Object> getSentinelStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("dashboard", dashboard);
        result.put("transportPort", transportPort);
        result.put("apiDefinitions", GatewayApiDefinitionManager.getApiDefinitions());
        result.put("flowRules", GatewayRuleManager.getRules());
        result.put("paramFlowRules", ParamFlowRuleManager.getRules());
        return result;
    }

}
