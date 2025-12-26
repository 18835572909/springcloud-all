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

1. 启动不成功：  一言难尽，多是版本冲突或者seata配置错误 (-.-)

2. 全局事务不生效
   - 异常类型没有指定，抛出的异常没有触发回滚
   - openfeign没有传递xid，导致被调用方没有回滚
     - 使用RootContext.getXid(): 确定xid传递情况，如果确实没有传递，添加拦截器，添加xid到header信息中。
     - 使用hystrix会导致xid不传递: hystrix使用线程组，ThreadLocal通病--子线程丢失参数（这种情况度娘会教你，太啰嗦）
   - xid传递成功，但是被调用方还是没有回滚
     - 检测是否开启数据源代理，保证seata正常代理

#### TCC模式试用

1. 在TM段使用@GlobalTransaction，标识seata全局事务管理

2. 基于面向接口编程的思想，定义TCC不同阶段要执行的方法：@LocalTCC、@TwoPhaseBusinessAction

3. seata支持的处理TCC常见3大问题：
   - @TwoPhaseBusinessAction(useTCCFence = true)
   - 在不同业务库，添加表：tcc_fence_log
     ```sql
      CREATE TABLE IF NOT EXISTS `tcc_fence_log`
      (
      `xid`           VARCHAR(128)  NOT NULL COMMENT 'global id',
      `branch_id`     BIGINT        NOT NULL COMMENT 'branch id',
      `action_name`   VARCHAR(64)   NOT NULL COMMENT 'action name',
      `status`        TINYINT       NOT NULL COMMENT 'status(tried:1;committed:2;rollbacked:3;suspended:4)',
      `gmt_create`    DATETIME(3)   NOT NULL COMMENT 'create time',
      `gmt_modified`  DATETIME(3)   NOT NULL COMMENT 'update time',
      PRIMARY KEY (`xid`, `branch_id`),
      KEY `idx_gmt_modified` (`gmt_modified`),
      KEY `idx_status` (`status`)
      ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4;
     ```
   
4. 简述TCC三大常见问题（[seata处理TCC常见问题](https://seata.apache.org/zh-cn/blog/seata-tcc-fence)）
   - 幂等：针对网络超时等其他因素导致 ‘TC’ 没有收到分支事务响应触发重试场景。避免重复执行代码
   - 空回滚：针对 ‘try’ 阶段，RM_1执行成功，RM_2异常没有执行 => ‘callback’ 阶段：RM_1执行回滚后，RM_2也执行回滚，这时RM_2会执行空回滚，eg：账户本来没有扣除，但是现在要执行增加，导致数据错误
   - 事务悬挂：类似空回滚情况，‘try’ 阶段RM_2只是超时但是收到了请求阻塞，在 ’callback‘ 阶段后，RM_2的执行 ‘try’ 操作，但是事务没有后续，导致事务悬挂