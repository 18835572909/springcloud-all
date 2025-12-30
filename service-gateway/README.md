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