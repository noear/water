
# Water 部署说明

## 环境要求说明

* mysql8：做为主库（字符集：utf8mb4，排序集：utf8mb4_general_ci）
* memcached：做为缓存使用
* redis：做为分布式锁、ID生成器、数据临时队列用
* mongodb：做为消息持久化用（也可以使用 mysql8）
* jdk11：做为运行时用
* nginx：做为反向代理使用

> 建议使用 centos7+ 部署；生产环境最少 4台服务器（接口服务*2、管理服务*1、消息派发*1）；开发环境1台即可；

## 开始部署

### 1、初始化数据库（参考db目录下的sql文件）

* water
* water_bcf
* water_log
* water_msg
* water_paas

### 2、进入数据库修改相关链接配置

1. 进入 water 库 water_cfg_properties 表，修改相关配置值（复制到外部编辑器修改，改完再替换回去）：

```yaml
#a) 以下配置描述格式为：tag/key，对应数据表的tag字段和key字段
#b) 里面的值比较复杂，所以需要复制到外部编辑器修改；注意不要破坏格式

water/water             #water 数据库的链接配置

water/water_msg         #water_msg 数据库的链接配置
water/water_msg_queue   #reids 链接配置，用作消息临时队列（建议独享实例）
water/water_msg_store   #mongodb 或 mysql 链接配置，用作消息持久化（建议独享实例）

water/water_log         #water_log 数据库的链接配置
water/water_log_store   #water_log 数据库的链接配置（后期可以换成别的链接）

water/water_redis       #reids 链接配置，用作分布式锁、ID生成
water/water_cache       #memcached 链接配置，用作缓存

water/water_paas        #water_paas 数据库的链接配置

water_bcf/bcf.yml       #修改掉 memcached 链接配置 和 water_bcf 数据库连接配置
```

2. 进入 bin 目录 waterapi_ext/_db.properties 文件，修改 water 数据库的链接配置

### 3、部署流程说明

1. 先启动 waterapi.jar
2. 配置 nginx，完成 water 域的监听，并转发给 waterapi.jar（water 默认使用了80端口，所有需要反向代理）
3. 然后给所有使用water服务的机器，添加 water host 记录
4. 最后依次启动 wateradmin.jar、waterpass.jar、waterraas.jar、watersev.jar

### 4、部署方案参考（参考bin目录下的jar文件；建议配置成System Service进行控制）

#### （一）生产环境建议方案
**服务器2台（2c4g）**

```
#接口服务（--white=0，可关闭IP限制。全部配置结束后，去掉）
#每台运行两个实例，共4个实例；外层配负载均衡

java -jar waterapi.jar --server.port=9370 --white=0  
java -jar waterapi.jar --server.port=9371 --white=0
```


**服务器1台（2c4g）**

```
#管理后台（--white=0，可关闭IP限制。全部配置结束后，去掉）
java -jar wateradmin.jar --server.port=9373 --white=0

#RaaS 运行服务（可选部署） 
java -jar waterraas.jar --server.port=9374  

#PaaS 接口运行服务           
java -jar waterpaas.jar --server.port=9376   

#工具后台服务        
java -jar watersev.jar --server.port=9372 --sss=tool    
```

**服务器1台（2c8g）**

```
#PaaS 定时任务运行服务
java -jar watersev.jar --server.port=9372 --sss=pln   
```


**服务器1台（2c8g）**

```
#消息派发服务（部署4个运行实例）
java -jar watersev.jar --server.port=9311 --sss=msg   
java -jar watersev.jar --server.port=9312 --sss=msg   
java -jar watersev.jar --server.port=9313 --sss=msg   
java -jar watersev.jar --server.port=9314 --sss=msg   
```

#### （二）开发环境建议方案

**服务器1台（1c2g）**

```
#接口服务（--white=0，可关闭IP限制。全部配置结束后，去掉）
java -jar waterapi.jar --server.port=9371 --white=0   

#管理后台（--white=0，可关闭IP限制。全部配置结束后，去掉）
java -jar wateradmin.jar --server.port=9373 --white=0

#RaaS 运行服务（可选部署） 
java -jar waterraas.jar --server.port=9374  

#PaaS 接口运行服务           
java -jar waterpaas.jar --server.port=9376   

#工具后台服务        
java -jar watersev.jar --server.port=9372 
```

### 5、后续配置修改

成功进入wateradmin管理后台后，打开 "管理管理 / 属性配置"。 进一步修改配置：

```yaml
water/paas_uri  #修改为waterpaas服务的http协议地址
water/raas_uri  #修改为waterraas服务的http协议地址
```

修改完成后，重启wateradmin服务（之后，就可以在wateradmin上调试paas和raas服务）。

### 附：补充说明

* water 的访问控制，基于ip安全名单实现。但部署时，不便于白名单添加。可以通过启动参数关闭：

> --white=0

* water 管理抬台初始密码

> bcf1234

* 使用 nginx 为 waterapi 服务添加 water 域 80 端口监听支持

> 建议生产环境仅限内网访问

* 在使用 water 的服务器上，添加 water 域的 host 记录

* 开发环境，且单机部署时，可以加这一批host记录

```yaml
127.0.0.1 water 
127.0.0.1 memcached.water.io memcached.dev.io 
127.0.0.1 redis.water.io redis.dev.io
127.0.0.1 mongo.dev.io
127.0.0.1 mysql.water.io mysql.dev.io
```

* 在linux下建议用配置成service，由 systemctl 命令管理（以waterapi、wateradmin为例）

```ini
#
#add file: /etc/systemd/system/waterapi.service
[Unit]
Description=waterapi
After=syslog.target

[Service]
ExecStart=/usr/bin/java -jar /data/sss/water/waterapi.jar --white=0
SuccessExitStatus=143
SuccessExitStatus=143
Restart=on-failure

[Install]
WantedBy=multi-user.target

#
#add file: /etc/systemd/system/waterapi.service
[Unit]
Description=wateradmin
After=syslog.target

[Service]
ExecStart=/usr/bin/java -jar /data/sss/water/wateradmin.jar --white=0
SuccessExitStatus=143
SuccessExitStatus=143
Restart=on-failure

[Install]
WantedBy=multi-user.target


# 操控命令部分参考：
# systemctl restart waterapi  #重启服务
# systemctl enable waterapi   #启用服务（系统重启时，自动启动该服务）
# systemctl start waterapi    #启动服务
# systemctl stop waterapi     #停止服务
```