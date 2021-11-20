#### 2.3.4
* 服务 watersev，下属二级服务增加各自的签到。从而形成各自服务的服务名
* 服务 watersev-pln，可自由集群，并让每个任务分布到集群中运行
* 服务 watersev-mot，可自由集群，并让每个任务分布到集群中运行
* 服务 watersev-msgexg，可自由集群，并分解borker到不同节点上
* 服务 watersev-msgdis，可自由集群，并分解borker到不同节点上
* 服务 watersev-sevchk，取消启动时需要 reset 的逻辑
* 服务 watersev，清理吃掉异常的情况
* 服务 wateradmin，清理吃掉异常的情况
* 镜像 waterapi 端口改为：9371
* 主库、PaaS库，时间改为 bigint
* 服务 waterraas 端口改为：9375 （之前为：9376）
* 服务 waterpaas 服务更名：waterfaas
* 表 paas_file 更名：luffy_file
* 表 paas_etl  更名：luffy_etl
* 管理后台 /paas/ 开头的路径，改为 /luffy/开头
* 配置 paas_uri 更名为：faas_uri
* 监视 paas_file 更名为：luffy_file
* 服务 waterapi 优化日志格式


#### 2.3.3 
* 修复 EncryptUtils 类，还 iv 时，加密出错的问题

#### 2.3.2
* CacheUtils ，增加自动构建本地缓存（驱动类型为：local）

#### 2.3.0.1
* 消息订阅支持 服务名模式订单（不再需要域名调用）***//未验证,这很重要

#### 2.3.0
* 完成 water 助手项目开发
* 消息总线，增加消息管道概念[Broker]并支持负载均衡（即多存储块）和本地队列方案[local]
* 日志服务增加更多的字段支持(weight,metainfo,tag4,service)
* 所有日志相关的都改成 water_log 架构，统一管理：
  * water_exam_log_bcf 更名为：water_log_sql_b
  * water_exam_log_sql 更名为：water_log_sql_p
* 数据架构调整
  * water_msg 元信息合到 water 库（[water_msg_subscriber][water_msg_topic]）
  * water_msg_soter? 取消 topic_id 的需要（可完全独立）
  * raas 相关表移到 water_paas 库 [rubber_*]
    * rubber_log_request 归到 water_paas 库（或可独立到 water_paas_request 库）
    * rubber_log_request_all 归到 water_paas 库（或可独立到 water_paas_request 库）


* water.client::MessageApi 增加 broker 参数


#### 2.2.18
* 基于增加 memcache 与 rediscache 自由切换的支持
* 增加 water_lob_store 的 es 存储
* 实现 Logger source 即时同步热切换!!!

#### 2.2.16
* 整个项目基于solon cloud接口进行开发（即基于 water-solon-plugin ）

#### 2.2.15
* 基于 weed3 3.4.0 进行改造

#### 2.2.8 - 2021.08.17
* 数据监控，添加FaaS支持（同时兼容旧的sql模式）
* 服务监控，增加图表显示

#### 2.2.6 - 2021.07.20
* 增加非稳定的消息订阅，自动删除处理
* 实现计划任务调度器集群能力；原执行中的状态限制会失效；秒数间隔的会与执行时长结合起来控制
* 实现 wateradmin 查看 `服务运行时状态` 的监控图表功能
* 解决 k8s 环境，wateradmin 不能查看 `服务运行时状态` 的安全问题 //java -jar xxx.jar -white=0
* 修复定时任务的时区配置不能有效执行的问题
* 日志查询，添加时区显示

#### 2.1.1 - 2021.05.20
* 去 fastjson 化

#### 2.1.0 - 2021.05.17
* water.client 完全去 solon 化

#### 2.0.32 - 2021.04.27
* 调整RedisX 的链接池超时时间(200->3000)

#### 2.0.30 - 2021.04.27
* 日志id，改为雪花算法生成
* 消息id，改为雪花算法生成

#### 2.0.29 - 2021.04.27
* 添加日志错误数量统计
* 服务注册，增加服务分组功能（tag）//为后台分组提供支持
* 告警增加基于tag的推送
* 日志统计，改为流式处理
* 消息统计，改为流式处理


#### 2.0.28 - 2021.04.25
* 服务监控/日志列表添加监控链接
* 消息总线/主题列表添加监控链接
* 将日志统计，改为流式方案（原匹式统计，量大了统计不动）
* 将消息统计，改为流式方案（同上...）
* 提交定时消息时，日期由yyyy-MM-dd HH:mm:ss 改为时间截形式（适应国际化）
* Track数据进数据库时，改为 Pipeline 模式批量插入

#### 2.0.26
* 1.取肖原1-6点之间不检测的策略
* 2.[paas_file]增加[use_whitelist]字段
* 3.[water_msg_message][water_msg_message_all]增加[last_date]字段