# 网关层通用实现

#### 常见网关层功能

- 路由转发
- 认证和鉴权
- 限流和熔断
- 格式化相应和请求
- 跨域支持
- 日志记录和监控
- 灰度支持
- ab测支持
- API结果聚集（基本不会，主要由业务方处理）
- 请求响应的解码编码

#### 常见开源网关项目

- Apache ShenYu (前身 Soul) [github-shenyu](https://github.com/apache/shenyu)
- sentinel + gateway
- nacos + gateway
- pig（积木云）              [github-pig](https://github.com/pigxcloud/pig)
- bladex（商业级网关）        [github-blade](https://github.com/chillzhuang/blade-tool)
- youlai-mall（电商网关）     [github-youlai](https://github.com/hxrui/youlai-mall)

#### AI总结
- 学习阶段：先从简单的 demo 开始
- 小型项目：使用 Spring Cloud Gateway + 自定义过滤器
- 中型项目：使用 pig 或 youlai-mall
- 大型企业：使用 Apache ShenYu 或 Spring Cloud Alibaba
- 定制需求：基于开源项目二次开发


#### 网关模式： springcloud + gateway + sentinel 

1. 提供两种资源维度的限流
    - Route 维度：即在 Spring 配置文件中配置的路由条目，资源名为对应的 routeId
    - API 维度：用户可以利用 Sentinel 提供的 API 来自定义一些 API 分组

 2. 规则类型 : 以下的rule-type
   ```yaml
   spring:
     cloud:
       sentinel:
         datasource:
           gw-flow:
             nacos:
               server-addr: localhost:8848
               dataId: gw-flow-rules
               groupId: DEFAULT_GROUP
               rule-type: gw-flow
   ```
   |  规则类型   | 数据源配置名       | 对应规则类  | 用途                |
   |  ----  |--------------| ----  |-------------------|
   | 普通流控规则  | flow         | FlowRule | 普通微服务的限流规则        |
   | 降级规则  | degrade      | DegradeRule | 熔断降级规则            |
   | 热点参数规则  | param-flow   | ParamFlowRule | 热点参数限流            |
   | 系统规则  | system         | SystemRule | 系统保护规则            |
   | 授权规则  | authority         | AuthorityRule | 黑白名单控制            |
   | 网关流控规则  | gw-flow      | GatewayFlowRule | Gateway 的限流规则     |
   | API分组规则  | gw-api-group | ApiDefinition | Gateway 的自定义API分组 |

3. 具体规则配置，可以参考 [gw-flow-rules.md](src/main/resources/gw-flow-rules.md)

4. 设置自定义异常 [ExceptionHandlerConfiguration.java](src/main/java/com/hz/voa/conf/ExceptionHandlerConfiguration.java)
   
5. 总结：
   1. Sentinel提供两大类型的Rule，一种是普通服务的Rule，一种是专门Gateway的规则
   2. 使用gateway的时候 rule-type: gw-flow
   3. spring.cloud.sentinel.filter.enabled=false， 才能使用RouteId（官网也有介绍）
   4. 核心的几个类： SentinelGatewayFilter、及上面的7个Rule

#### 相关参考
[Sentinel官网Wiki](https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel)
