#### 2.0.X 规划::
* 监控数据获取，改成基于协议
* 开发项目部署功能
* 日志器适配MangoDb、HBase、ES


#### 2.0.9 规划::
* 调整xcall,xclient不：http,client

#### 2.0.8 规划::
* 增加 ConfigM 配置块内引用

#### 2.0.7.1 规划::
* 添加springboot适配：water.client-springboot-starter
* 升级solon 到 1.2.16 版本

#### 2.0.4 规划::
* 增加默认日志器配置(water.logger)
* 增加IP漂移兼容处理
* waterapi内部日志改为管道模式
* 日志器增加报警配置，不再需要数据监视
* 完善 WaterClient.Config.set(k,v) 机制
* 增加链路相关性跟踪机制
* waterapi 改为新的开发模式
* 完成dubbo注册服务适配

#### 2.0.2 规划::
* 增加PoalDb支持
* 消息队列::增加新的RabbitMQ、RocketQM消息队列协议适配
* 日志::增加MangoDb的协议适配
* 优化::日志改为管道模式提交***
* 优化::性能监控客户端改为缓冲模式提交***
* 优化::白名单前端增加10s本地缓存***
* 优化::将配置服务与其它服务地址做分离支持***
* 优化::XWaterUpstream（减少更新时的锁时间）
* 优化::网关管理与处理机制

#### 2.0.1
* 重构PaaS，基于SolonJT
* 重构日志服务
* 重构配置服务
* 重构白名单服务
* 重构Ops管理模块
* 重构Dev管理模块
* 取消账号体系