#### 2.5.7
* 修复 waterapi 没日志记录的问题
* 添加 关于页
* solon 升级为：1.6.30
* grit 升级为：1.0.8
* weed3 升级为：3.4.19
* snack3 升级为：3.2.16

#### 2.5.6
* water job 支持传递参数
* 升级 sponge_track 的统计 sql(符合 mysql 5.7 的模式)

#### 2.5.5
* 日志服务的 es 方案改为 data steam 模式

#### 2.5.4
* 升级 redis，支持 user 配置（无用的user要去掉了）

#### 2.5.3
* 升级 solon, snack3（有兼容性变化）, weed3

#### 2.5.2
* 取消 getDistributionListByMsg 接口缓存，不然会造成消息重派
* 修复行为记录时，没有记录行为者的ip问题（涉及water.client更新）
* 取消 镜像统一端口8080，改为各自不冲突端口

#### 2.5.1
* /run/,/msg/ 开头的触发规则统一为：/_run/ 开头
* wateraide 更名为：xwater
* 增加运行时状态获取令牌安全模式

#### 2.5.0
* 将权限系统切换为grit

#### 2.4.2
* 增加分布任务调度令牌安全模式

#### 2.4.1
* 增加服务规模与消息主题规模适配支持
* 消息总线/主题列表，增加 主题规模小等、中等的布局适配
* 消息总线/主题列表，增加 主题规模小等、中等的支持；以前服务规模大等的布局适配
* 服务监控/网关监控，增加 服务规模小等、中等的布局适配
* 服务监控/服务状态，增加 服务规模小等、中等、大等的布局适配
* 服务监控/节点性能，增加 服务规模小等、中等、大等的布局适配
* 服务监控/接口性能，增加 服务规模小等、中等、大等的布局适配
* 服务监控/SQL性能，增加 服务规模小等、中等、大等的布局适配
* 服务监控/日志数量，增加 服务规模小等、中等的布局适配
* 服务监控/行为记录，增加 服务规模小等、中等、大等的布局适配

#### 2.4.0
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
* 增加 token 的白名单支持
* 优化 大量表结构


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


#### 2.0.X 规划::
* 监控数据获取，改成基于协议
* 开发项目部署功能
* 日志器适配MangoDb、HBase、ES
* 消息入队处理，改为重消息方案（更利于扩展派发能力）

#### 2.0.14 规划::
* 调整 water.client 及 water.client-solon-plugin
* 尝试构建基于 solon.cloud 标准的接口：water-solon-plugin

#### 2.0.9 规划::
* 调整xcall,xclient为：http, client

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