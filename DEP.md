
# Water 部署说明

## 环境要求说明

* mysql8：做为主库
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

1. 进入 water 库 water_cfg_properties 表，修改相关配置（复制出值，在外部编辑器里修改，再替换掉）：

```yaml
water/water             #water 数据库的链接配置

water/water_msg         #water_msg 数据库的链接配置
water/water_msg_queue   #reids 链接配置，用作消息临时队列
water/water_msg_store   #mongodb 链接配置，用作消息持久化（也可以用 mysql）

water/water_log         #water_log 数据库的链接配置
water/water_log_store   #water_log 数据库的链接配置（后期可以换成别的链接）

water/water_redis       #reids 链接配置，用作分布式锁、ID生成器
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

### 5、补充说明

* water 的访问控制，基于ip安全名单实现。但部署时，不便于白名单添加。可以通过启动参数关闭：

> --white=0

* water 管理抬台初始密码

> bcf1234

* 使用 nginx 为 waterapi、wateradmin、waterpaas、waterraas 服务添加域解析支持

> 其中 waterapi，必须增加 water 域解析支持（建议仅限内网访问）

* 在使用 water 的服务器上，添加 water 域的 host 记录