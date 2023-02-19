#### 2.10.0
* solon 升级为: 2.0.0
* snack3 升级为：3.2.52
* grit 升级为：1.5.0
* rock 升级为：2.6.0
* luffy 升级为：1.5.1
* 优化框架任务 speed_sync_date，speed_sync_hour，speed_sync 执行性能（提高几倍）
* 优化框架任务 log_stat_sync 执行性能（提高几倍）
* 函数计算增加事务控制快捷接口, XUtil.tran(()=>{})

#### 2.9.3
* wateradmin, 增加 "上游配置" 禁用、删除时状态同步（即发消息）
* wateradmin, 修复 "上游监控" /检测 404 的问题
* wateradmin, 修复 upstream 更新后，不能同步的问题 //由 water-solon-cloud-plugin 1.11.5 引起
* waterapi, 获取上游配置时增加是否启用过滤
* 增加 日志查询单条最大显示长度设置（默认不限）
* solon 升级为：1.12.2
* grit 升级为：1.4.4
* rock 升级为：2.5.4
* luffy 升级为：1.4.3

#### 2.9.2
* 移除 "负载监控","主机监控","存储监控"（需要与云服务对接，用起来麻烦）
* 增加 日志查询单条最大显示长度设置
* solon 升级为：1.12.1 //有问题，water-solon-cloud-plugin 1.11.5
* snack3 升级为：3.2.50
* wood 升级为：1.0.7
* grit 升级为：1.4.3

#### 2.9.1
* 调整 "证书监视" 界面，报警间隔为1小时
* 调整 HttpURLConnection 缓存处理
* wateradmin 改由 smart-http 驱动
* solon 升级为：1.10.13
* snack3 升级为：3.2.46
* wood 升级为：1.0.2

#### 2.9.0
* solon 升级为：1.10.7
* 调整 orm 框架，改由 wood 代替

#### 2.8.2
* 调整 "证书监视" 界面，增加检测情况列
* 调整 "应用监视" 界面，增加检测情况列
* solon 升级为：1.10.0


#### 2.8.1
* 新增 "指标关注" 记录接口和显示
* 新增 "证书监视" 管理界面 
* 添加 "应用监视" 批量禁用启用支持
* 添加 "数据监视" 批量禁用启用支持

#### 2.8.0
* wateradmin 增加 gritapi 服务注册
* TrackApi 改用 http 代理提交数据（不再直连redis）!!!

#### 2.7.3
* 权限管理增加分组授权支持

#### 2.7.2
* 更名 "服务监控" 为："系统监控"
* 新增 "服务发现"(排序为 4，方便对映行业概念)
* 新增 "服务发现/服务列表"(由原"服务监控"的管理功能独立出来)
* 转移 "配置服务/上游配置" 到："服务发现/上游配置"


#### 2.7.1
* 更名 "日常工具" 为："告警工具"
* 新增 "应用监视"（放到 "数据监视" 下面）
* 简化 "数据监视" 输入表单，优先告警出处

#### 2.7.0
* solon 升级为：1.8.1（调整了插件接口，须配套升级）
* grit 升级为：1.2.0
* snack3 升级为：3.2.26
* 增加钉钉通知支持
* 增加企业微信通知支持
* 简化数据监视功能

#### 2.6.3
* [控制台] 所有导入功能统一为插入或替换（之前有些插入，有些是替换）
* [控制台] 调整部分列表删除后，跳转到同状态列表
* [控制台] 修复定时任务监控无数据的问题
* [控制台] UI优化
* HttpUtils 工具类，支持超时设置
* runJob，超时改为：60*5
* runStatus，超时改为：10
* solon 升级为：1.7.5
* redisx 升级为：1.4.3

#### 2.6.2
* 设置增加菜单资源管理能力
* 网关配置，更名为：上游配置
* 网关监控，更名为：上游监控
* 调整列表工具栏样式
* 多语言包增加批量删除功能
* 消息持久转移调整为log_date为条件
* 控制台登录界面增加版本显示
* 控制台UI优化
* solon 升级为：1.7.3
* grit 升级为：1.1.4
* redisx 升级为：1.4.2
* snack3 升级为：3.2.21


#### 2.6.1
* 客户端增加多地址集群支持
* 增加消息交换性能监视
* 增加消息重置条件last_fullltime
* solon 升级为：1.7.2
* grit 升级为：1.1.2
* redisx 升级为：1.4.1
* snack3 升级为：3.2.20

#### 2.6.0
* 添加密钥配置模块
* 添加国际化配置模块
* 优化日志数据监控的线图显示
* 修复保存消息主题后界面错乱的问题
* ConfigHandler::handler 更名为 handle
* DiscoverHandler::handler 更名为 handle
* MessageHandler::handler 更名为 handle
* WaterClient.job 更名为 Job

#### 2.5.9
* 添加查询记录和可复用选择
* 日志ES方案，增加8.x支持
* 调整 HttpUtils 添加短处理和长处理的切换
* 修复部分无tag的聚合导航不能点中的问题
* 忽略管理后台重启时，出现jwt密钥文件找不到的问题
* solon 升级为：1.6.36
* grit 升级为：1.0.11
* redisx 升级为：1.0.6
* esearchx 升级为：1.0.11（支持 Elasticsearch 8.x）

#### 2.5.8
* 全部菜单改成中文名
  * 原Faas频道更名为："函数计算"
  * 原Dev频道更名为："开发助手"
* 所有外部CDN资源，都集成到程序内里了（管理后台胖了6Mb） 
* 原 “计算资源” 从 “Ops” 移到 “配置管理” 下面
* 取消 "规则计算" 模块（独立为一个新的项目）
* 取消 "Ops" 模块
* solon 升级为：1.6.33
* grit 升级为：1.0.9

#### 2.5.7
* 修复 waterapi 没日志记录的问题
* 添加 关于页
* solon 升级为：1.6.30
* grit 升级为：1.0.8
* snack3 升级为：3.2.16

#### 2.5.6
* water job 支持传递参数
* 升级 sponge_track 的统计 sql(符合 mysql 5.7 的模式)

#### 2.5.5
* 日志服务的 es 方案改为 data steam 模式
  * ::所有使用 es 的日志记录器，都要重新保存一下

#### 2.5.4
* 升级 redis，支持 user 配置（无用的user要去掉了）
  * ::已有 redis 配置，无用的user属性要去掉（如果之前是随便写的...）

#### 2.5.3
* 升级 solon, snack3（有兼容性变化）

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
* 整个项目基于solon cloud接口进行开发（即基于 water-solon-cloud-plugin ）

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
* 尝试构建基于 solon.cloud 标准的接口：water-solon-cloud-plugin

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