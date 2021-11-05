# Water k8s 部署说明

## 请在完整的看完文档后，再开始动手!!!

## 一、环境要求说明（请准备好）

* mysql8：做为主库（字符集：utf8mb4，排序集：utf8mb4_general_ci）
* redis：做为分布式锁、数据临时队列用
* mongodb：做为消息持久化用（也可以使用 mysql8）
* jdk11：做为运行时用（一定要用JDK11）
* nginx：做为反向代理使用

> 建议使用 centos7+ 部署；生产环境最少 4台服务器；开发环境1台即可。

## 二、初始化环境

运行 Water 助理服务（在本地或服务器上运行都可）

```properties
java -Dfile.encoding=utf-8 -jar /data/sss/water/wateraide.jar
```

* 用浏览器打开界面：`http://locahost:19371`，按提示操作
* 初始化完成后，进入 [安全名单] 添加相关服务器的ip

| 名单列表 | 说明 |
| -------- | -------- |
| master     | 添加所有 water 服务器的ip（一般是内网ip）     |
| server     | 添加所有会用到 water 服务的服务器的ip（一般是内网ip）     |
| client     | 添加所有操作 water 后台的电脑的ip（一般是外网ip）     |

* 完成操作后，关掉服务(有需要再启动，每次用完都关掉)

## 三、开始部署服务

#### 1、测试 waterapi 服务
> `waterapi.jar` 和 `waterapi_ext/` 必须在一起

* 修改 `waterapi_ext/_db.properties` 的配置
* 然后运行 `java -jar waterapi.jar`
* 如果出错，则检查相关配置。直到成功为止

#### 2、部署流程说明

1. 启动 waterapi.jar
2. 配置 nginx，完成 `waterapi` 域的监听，并转发给 waterapi.jar（`waterapi` 默认使用了80端口，要需要反向代理）
3. 然后给所有使用 water 服务的机器，添加 `waterapi` host 记录（进 /etc/hosts 修改）
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

* 使用 nginx 为 waterapi 服务添加 `waterapi` 域 80 端口监听支持

> 建议生产环境仅限内网访问

* 在使用 water 的服务器上，添加 `waterapi` 域的 host 记录

```yaml
127.0.0.1 water #ip为waterapi服务的地址
```

* 开发环境，且单机部署时，可以加这一批host记录

```yaml
127.0.0.1 waterapi 
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

* `waterapi` 域的nginx配置示例（注意真实的ip转发）

```ini
upstream waterapi{
    server 127.0.0.1:9370 weight=10;
    server 127.0.0.1:9371 weight=10;
}
server{
    listen 80;
    server_name waterapi;
    
    location / {
        proxy_pass http://waterapi;
        proxy_set_header  X-Real-IP  $remote_addr;
        proxy_set_header  X-Forwarded-For  $proxy_add_x_forwarded_for;
        proxy_set_header  Host $http_host;
    }
}
```