package com.hz.voa.conf;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.Rule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author rhb
 * @date 2026/1/5 13:41
 **/
@Slf4j
@Configuration
public class ExceptionHandlerConfiguration {
    //
    //public ExceptionHandlerConfiguration(){
    //    BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
    //        @Override
    //        public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
    //            Map<String, String> result = new HashMap<>(3);
    //            result.put("code", String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()));
    //            result.put("message", HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
    //            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
    //                    .contentType(MediaType.APPLICATION_JSON)
    //                    .body(BodyInserters.fromObject(result));
    //        }
    //    };
    //
    //    // 加载自定义限流异常处理器
    //    GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    //}


    public ExceptionHandlerConfiguration(){
        BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                // 打印当前所有的规则
                System.out.println("=== 当前Gateway规则 ===");
                Set<GatewayFlowRule> gatewayRules = GatewayRuleManager.getRules();
                for (GatewayFlowRule rule : gatewayRules) {
                    System.out.println(rule);
                }
                System.out.println("=== 当前ParamFlow规则 ===");
                List<ParamFlowRule> paramRules = ParamFlowRuleManager.getRules();
                for (ParamFlowRule rule : paramRules) {
                    System.out.println(rule);
                }
                System.out.println("=== 当前Flow规则 ===");
                List<FlowRule> flowRules = FlowRuleManager.getRules();
                for (FlowRule rule : flowRules) {
                    System.out.println(rule);
                }


                throwable.printStackTrace();
                // 获取请求路径
                String path = serverWebExchange.getRequest().getPath().toString();

                // 获取触发限流的规则信息
                String blockedResource = "未知资源";
                String ruleLimit = "未知限制";
                String blockType = "未知类型";
                int resourceMode = -1;

                if (throwable instanceof BlockException) {
                    BlockException blockException = (BlockException) throwable;
                    blockedResource = blockException.getRule().getResource();

                    // 获取规则对象
                    Object rule = blockException.getRule();

                    try {
                        Class<?> gatewayFlowRuleClass = Class.forName("com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule");
                        if (gatewayFlowRuleClass.isInstance(rule)) {
                            blockType = "网关流控规则";

                            // 通过反射获取网关规则详情
                            try {
                                // 获取 resourceMode
                                Method getResourceModeMethod = gatewayFlowRuleClass.getMethod("getResourceMode");
                                resourceMode = (int) getResourceModeMethod.invoke(rule);

                                // 获取 count
                                Method getCountMethod = gatewayFlowRuleClass.getMethod("getCount");
                                double count = (double) getCountMethod.invoke(rule);
                                ruleLimit = String.format("网关QPS限制: %d/秒", (int) count);

                                // 获取 controlBehavior
                                Method getControlBehaviorMethod = gatewayFlowRuleClass.getMethod("getControlBehavior");
                                int controlBehavior = (int) getControlBehaviorMethod.invoke(rule);

                                // 获取 paramItem（热点参数）
                                Method getParamItemMethod = gatewayFlowRuleClass.getMethod("getParamItem");
                                Object paramItem = getParamItemMethod.invoke(rule);

                                if (paramItem != null) {
                                    blockType = "网关热点参数规则";
                                    Class<?> paramItemClass = Class.forName("com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem");
                                    Method getFieldNameMethod = paramItemClass.getMethod("getFieldName");
                                    String fieldName = (String) getFieldNameMethod.invoke(paramItem);
                                    ruleLimit = String.format("网关热点参数[%s]限制: %d/秒", fieldName, (int) count);
                                }
                            } catch (Exception e) {
                                // 反射失败，使用默认信息
                                ruleLimit = "网关规则限制";
                            }
                        } else if (rule instanceof FlowRule) {
                            FlowRule flowRule = (FlowRule) rule;
                            ruleLimit = String.format("QPS限制: %d/秒", (int) flowRule.getCount());
                            blockType = "流控规则";
                        } else if (rule instanceof DegradeRule) {
                            DegradeRule degradeRule = (DegradeRule) rule;
                            ruleLimit = String.format("熔断规则: RT=%.2fms, 比例=%.2f", degradeRule.getCount(), degradeRule.getSlowRatioThreshold());
                            blockType = "熔断规则";
                        } else if (rule instanceof ParamFlowRule) {
                            ParamFlowRule paramRule = (ParamFlowRule) rule;
                            ruleLimit = String.format("热点参数: QPS=%d", (int) paramRule.getCount());
                            blockType = "热点规则";
                        }
                    }catch(ClassNotFoundException e){
                        log.error("BlockRequestHandler Error : {}", ExceptionUtil.stacktraceToString(e));
                    }

                    // 3. 打印到控制台
                    log.info("======= Sentinel 触发限流 =======");
                    log.info("触发时间: {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    log.info("请求路径: {}", path);
                    log.info("资源名称: {}", blockedResource);
                    log.info("规则类型: {}", blockType);
                    log.info("限制条件: {}", ruleLimit);
                    log.info("异常信息: {}", blockException.getClass().getSimpleName());
                    log.info("请求参数: {}", serverWebExchange.getRequest().getQueryParams());
                    log.info("请求头: {}", serverWebExchange.getRequest().getHeaders().getFirst("User-Agent"));
                    log.info("================================");
                }

                // 4. 记录到日志文件
                String logMessage = String.format("Sentinel限流触发 - 资源: %s, 路径: %s, 类型: %s, 限制: %s", blockedResource, path, blockType, ruleLimit);
                log.warn(logMessage);

                // 5. 返回响应
                Map<String, Object> result = new LinkedHashMap<>();
                result.put("code", HttpStatus.TOO_MANY_REQUESTS.value());
                result.put("message", "请求过于频繁，请稍后再试");
                result.put("timestamp", System.currentTimeMillis());
                String finalBlockedResource = blockedResource;
                String finalBlockType = blockType;
                String finalRuleLimit = ruleLimit;
                result.put("data", new HashMap<String, Object>() {{
                    put("blockedResource", finalBlockedResource);
                    put("blockType", finalBlockType);
                    put("ruleLimit", finalRuleLimit);
                    put("path", path);
                }});

                return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(result));
            }
        };
        // 加载自定义限流异常处理器
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }


}
