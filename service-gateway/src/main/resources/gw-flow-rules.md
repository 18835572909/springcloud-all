```json
[
  {
    "resource": "order-service",   
    "resourceMode": 0,            
    "grade": 1,                   
    "count": 1,                  
    "intervalSec": 1,              
    "controlBehavior": 0,          
    "burst": 0,                    
    "maxQueueingTimeoutMs": 0,     
    "paramItem": {                 
      "index": 0,
      "parseStrategy": 0,
      "fieldName": "",
      "pattern": "",
      "matchStrategy": 0
    }
  }
]
```

- resource: 资源名
- resourceMode: 
  - 0: 标识RouteId模式
  - 1: 标识API组模
- grade: 限流模式
  - 0: 线程数
  - 1: QPS
- count: 配合grade使用
- intervalSec: 时间窗口
- controlBehavior: 流量控制效果
  - 0：快速失败（直接拒绝）; 
  - 1：Warm Up（预热）; 
  - 2：排队等待; 
  - 3：Warm Up + 排队等待
- burst: 应对突发请求的额外配额
  - 仅当 controlBehavior=0或 1时有效 
  - 允许突发请求的数量 
  - 0 表示不允许突发
- maxQueueingTimeoutMs: 排队等待的最大时间
  - 仅当 controlBehavior=2或 3时有效
  - 请求在队列中等待的最长时间
  - 0 表示不等待，直接拒绝
- paramItem: 热点参数配置 (GatewayParamFlowItem)
  - index: 参数索引
  - parseStrategy: 参数解析策略 (SentinelGatewayConstants)
    - 0：URL 查询参数 
    - 1：HTTP 请求头
    - 2：Cookie
    - 3：来源 IP
    - 4：URL 路径变量
    - 5：请求体（需要额外配置）
  - fieldName: 参数名 -- 指定参数的名称
  - pattern: 参数值 正则匹配 
  - matchStrategy: 匹配策略  (SentinelGatewayConstants)
    - 0：精确匹配
    - 1：正则匹配
    - 2：包含匹配
 
