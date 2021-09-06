# Water 部署说明

## 请在完整的看完文档后，再开始动手!!!

## 环境要求说明

* mysql8：做为主库（字符集：utf8mb4，排序集：utf8mb4_general_ci）
* memcached：做为缓存使用
* redis：做为分布式锁、数据临时队列用
* mongodb：做为消息持久化用（也可以使用 mysql8）
* jdk11：做为运行时用（一定要用JDK11）
* nginx：做为反向代理使用

> 建议使用 centos7+ 部署；生产环境最少 4台服务器；开发环境1台即可。

## 开始部署

### 一、初始化数据库（参考db目录下的sql文件）

| 数据库 | 说明 |
| -------- | -------- |
| water | 主库 |
| water_bcf | 账号与权限库 |
| water_log | 日志库。包括特定日志和普通日志（普通日志，每个日志器可选择不同的存储） |
| water_msg | 消息库。存放主题、订阅关系以及消息（消息可通过配置存入mongodb） |
| water_paas | FaaS代码库。即放存FaaS的函数代码 |

### 二、进入安装模式初始化配置

#### 1、 运行安装模式： `java -jar wateradmin.jar -setup=1 -s=x -u=x -p=x` 

* -s 为主库地址，格式 ip:port
* -u 为链接账号
* -p 为链接密码

> 例：运行 `java -jar wateradmin.jar -server.port=9373 -setup=1 -s=localhost:3306 -u=root -p=1234` ； 然后，打开 http://localhost:9373 或打开对应的外网地址。

#### 2、 进入属性配置模块，修改以下相关配置值：

| 配置组 | 配置键 | 说明 |
| -------- | -------- | -------- |
| water     | water     | water 数据库的链接配置     |
| | | |
| water     | water_msg     | water_msg 数据库的链接配置     |
| water     | water_msg_queue     | reids 链接配置，用作消息临时队列（建议独享实例）     |
| water     | water_msg_store     | mongodb 或 mysql 链接配置，用作消息持久化（建议独享实例）     |
| | | |
| water     | water_log     | water_log 数据库的链接配置     |
| water     | water_log_store     | water_log 数据库的链接配置（后期可以换成别的链接）     |
| | | |
| water     | water_redis     | reids 链接配置，用作分布式锁、ID生成     |
| water     | water_cache     | memcached 链接配置，用作缓存     |
| | | |
| water     | water_paas     | water_paas 数据库的链接配置     |
| | | |
| water_bcf     | bcf.yml     | 修改掉 memcached 链接配置 和 water_bcf 数据库连接配置;同时修改server.session.state.domain为你的域名或服务器ip     |


#### 3、 进入安全名单模块，添加相关ip名单：


| 名单列表 | 说明 |
| -------- | -------- |
| master     | 添加所有 water 服务器的ip（一般是内网ip）     |
| server     | 添加所有会用到 water 服务的服务器的ip（一般是内网ip）     |
| client     | 添加所有操作 water 后台的电脑的ip（一般是外网ip）     |



#### 4、 进入 bin 目录 waterapi_ext/_db.properties 文件，修改 water 主库的链接配置

> 尝试运行 `java -jar waterapi.jar` 进行测试。如果没有出错，则停掉安装模式。

### 三、部署流程说明

1. 先启动 waterapi.jar
2. 配置 nginx，完成 water 域的监听，并转发给 waterapi.jar（water 默认使用了80端口，所有需要反向代理）
3. 然后给所有使用water服务的机器，添加 waterapi 和 waterapi.water host 记录（进 /etc/hosts 修改）
4. 再后依次启动 wateradmin.jar、waterpass.jar、waterraas.jar、watersev.jar

### 四、部署方案参考（参考bin目录下的jar文件；建议配置成System Service进行控制）

#### 1、 生产环境建议方案
**服务器2台（2c4g）**

```
#接口服务
#每台运行两个实例，共4个实例；外层配负载均衡

java -jar waterapi.jar --server.port=9370
java -jar waterapi.jar --server.port=9371
```


**服务器1台（2c4g）**

```
#管理后台（如果要不限ip访问，添加参考：--white=0）
java -jar wateradmin.jar --server.port=9373

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

#### 2、 开发环境建议方案

**服务器1台（1c2g）**

```
#接口服务
java -jar waterapi.jar --server.port=9371

#管理后台（如果要不限ip访问，添加参考：--white=0）
java -jar wateradmin.jar --server.port=9373

#RaaS 运行服务（可选部署） 
java -jar waterraas.jar --server.port=9374  

#PaaS 接口运行服务           
java -jar waterpaas.jar --server.port=9376   

#工具后台服务        
java -jar watersev.jar --server.port=9372 
```

### 五、后续配置修改

成功进入wateradmin管理后台后，打开 "管理管理 / 属性配置"。 进一步修改配置：

| 配置组 | 配置键 | 说明 |
| -------- | -------- | -------- |
| water     | paas_uri     | 修改为waterpaas服务的http协议地址（优先用域名）     |
| water     | raas_uri     | 修改为waterraas服务的http协议地址（优先用域名）     |

修改完成后，重启wateradmin服务（之后，就可以在wateradmin上调试paas和raas服务）。

> 其它一些配置，视情况进行调整。

### 附：补充说明

* water 的访问控制，基于ip安全名单实现。如果不需要，可以通过启动参数关闭：

> --white=0

* water 管理抬台初始账号与密码

> 账号：admin 密码：bcf1234

* 使用 nginx 为 waterapi 服务添加 waterapi 和 waterapi.water 域 80 端口监听支持

> 建议生产环境仅限内网访问

* 在使用 water 的服务器上，添加 waterapi 和 waterapi.water 域的 host 记录

```yaml
127.0.0.1 water #ip为waterapi服务的地址
```

* 开发环境，且单机部署时，可以加这一批host记录

```yaml
127.0.0.1 waterapi waterapi.water 
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
ExecStart=/usr/bin/java -jar /data/sss/water/waterapi.jar
SuccessExitStatus=143
Restart=on-failure

[Install]
WantedBy=multi-user.target

#
#add file: /etc/systemd/system/wateradmin.service
[Unit]
Description=wateradmin
After=syslog.target

[Service]
ExecStart=/usr/bin/java -jar /data/sss/water/wateradmin.jar
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

* water域的nginx配置示例（注意真实的ip转发）

```ini
upstream waterapi{
    server 127.0.0.1:9370 weight=10;
    server 127.0.0.1:9371 weight=10;
}
server{
    listen 80;
    server_name waterapi waterapi.water;
    
    location / {
        proxy_pass http://waterapi;
        proxy_set_header  X-Real-IP  $remote_addr;
        proxy_set_header  X-Forwarded-For  $proxy_add_x_forwarded_for;
        proxy_set_header  Host $http_host;
    }
}
```