# Seata多场景试用

### 版本 (单点服务)

- JDK: 1.8
- seata-server: 1.7
- SpringCloud: Hoxton.SR12
- SpringCloudAlibaba: 2.2.8.RELEASE

### AT模式试用

#### AT模式使用

1. 引入maven坐标，可以使用 boot-starter 或者 cloud-starter (前者版本指定便捷，网上说cloud-starter中提供xid的传递支持【没测试】)
```xml
    <!--下面两项二选其一-->
    <dependency>
        <groupId>io.seata</groupId>
        <artifactId>seata-spring-boot-starter</artifactId>
        <version>1.7.0</version>
        <exclusions>
            <exclusion>
                <groupId>com.alibaba.nacos</groupId>
                <artifactId>nacos-client</artifactId>
            </exclusion>
        </exclusions>
    </dependency>

    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
    </dependency>
```

2. 添加seata客户端配置：主要用于seata-server跟服务的通讯（这就是为啥配置要跟seata-server配置很多地方一样的原因）
```yaml
# Seata 配置
seata:
  enabled: true
  access-key: seata
  secret-key: SeataSecretKey0c382ef121d778043159209298fd40bf3850a017
  application-id: seata-server
  enable-auto-data-source-proxy: false    # 是否启用数据源bean的自动代理
  tx-service-group: default_tx_group
  service:
    disable-global-transaction: false
    vgroup-mapping:
      default_tx_group: default
  client:
    rm:
      report-success-enable: true  # 是否上报成功状态
      report-retry-count: 3   # 重试次数
  registry:
    type: nacos
    nacos:
      server-addr: localhost:8848
      namespace: f58a1bf4-7ad9-483b-a3f5-5b1b0be32ea7
      application: seata-server
      group: SEATA_GROUP
      username: nacos
      password: nacos
  config:
    type: nacos
    nacos:
      server-addr: localhost:8848
      namespace: f58a1bf4-7ad9-483b-a3f5-5b1b0be32ea7
      group: SEATA_GROUP
      username: nacos
      password: nacos
```

3. 开启数据源代理(很重要，这里是seata生效的入口)
```java
@EnableAutoDataSourceProxy
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
```

4. 业务使用样例。可以参考下面核心类：
- [OrderServiceImpl#create](service-order/src/main/java/com/hz/voa/service/impl/OrderServiceImpl.java)
- [StockServiceImpl#deduct](service-wms/src/main/java/com/hz/voa/service/impl/StockServiceImpl.java)

#### AT实现原理及相关源码

- 自动配置装载bean： @EnableAutoDataSourceProxy -> AutoDataSourceProxyRegistrar -> SeataAutoDataSourceProxyCreator
- 注解的AOP代理实现： @GlobalTransactional 

##### AT中常见FAQ

1. 启动不成功： (-.-)  一言难尽，多是版本冲突或者seata配置错误）

2. 全局事务不生效
   - 异常类型没有指定，抛出的异常没有触发回滚
   - openfeign没有传递xid，导致被调用方没有回滚
     - 使用RootContext.getXid(): 确定xid传递情况，如果确实没有传递，添加拦截器，添加xid到header信息中。
     - 使用hystrix会导致xid不传递: hystrix使用线程组，ThreadLocal通病--子线程丢失参数（这种情况度娘会教你，太啰嗦）
   - xid传递成功，但是被调用方还是没有回滚
     - 检测是否开启数据源代理，保证seata正常代理
