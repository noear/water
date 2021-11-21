# Water jar 模式，生产环境部署参考

## 请在完整的看完文档后，再开始动手!!!

## 一、环境要求说明（请准备好）

* mysql8：做为主库（字符集：utf8mb4，排序集：utf8mb4_general_ci）
* redis：做为分布式锁、数据临时队列用
* mongodb：做为消息持久化用（也可以使用 mysql8）
* elasticsearch：做为日志持久化用（也可以使用 mysql8 或 mongodb）
* jdk11：做为运行时用（一定要用JDK11）
* nginx：做为反向代理使用

> 建议使用 centos7+ 部署；生产环境最少 4台服务器。

## 二、初始化环境

运行 Water 助理（在本地或服务器上运行都可）

```properties
java -Dfile.encoding=utf-8 -jar wateraide.jar
```

* 用浏览器打开界面：`http://locahost:19371`，按提示操作
* 完成操作后，关掉服务(有需要再启动，每次用完都关掉)

## 三、开始部署服务

#### 1、测试 waterapi 服务
> `waterapi.jar` 和 `waterapi_ext/` 必须在同一目录

* 修改 `waterapi_ext/_db.properties` 的配置
* 然后运行 `java -jar waterapi.jar`
* 如果出错，则检查相关配置。直到成功为止

#### 2、部署流程说明

1. 启动 waterapi.jar
2. [配置 nginx](#nginx)，完成 `waterapi` 域的监听，并转发给 waterapi.jar
3. 给使用 water 服务的机器，添加 `waterapi` host 记录（进 /etc/hosts 修改）
4. 依次启动 wateradmin.jar、waterpass.jar、waterraas.jar、watersev.jar

## 四、部署方案参考 - 生产环境

**服务器2台（2c4g）**

```
#主接口服务
#每台运行两个实例，共4个实例；外层用 nginx 做负载均衡（如果要限制 ip 或 token 访问，添加参考：--white=1）

java -jar waterapi.jar --server.port=9370
java -jar waterapi.jar --server.port=9371
```


**服务器1台（2c4g）**

```
#后台服务（工具服务）        
java -jar watersev.jar --server.port=9372 --sss=tol   

#后台服务（消息交换机服务）        
java -jar watersev.jar --server.port=9321 --sss=msgexg   

#管理控制台（如果要限制ip访问，添加参考：--white=1）
java -jar wateradmin.jar --server.port=9373

#FaaS 接口运行服务           
java -jar waterfaas.jar --server.port=9374

#RaaS 运行服务（可选部署） 
java -jar waterraas.jar --server.port=9375      
```

**服务器1台（2c8g）**

```
#FaaS 定时任务运行服务
java -jar watersev.jar --server.port=9372 --sss=pln   
```


**服务器1台（2c8g）**

```
#消息派发服务（部署4个运行实例）
java -jar watersev.jar --server.port=9311 --sss=msgdis   
java -jar watersev.jar --server.port=9312 --sss=msgdis   
java -jar watersev.jar --server.port=9313 --sss=msgdis   
java -jar watersev.jar --server.port=9314 --sss=msgdis   
```

## 五、后续配置修改

进入 wateradmin 管理控制台，打开 "配置管理 / 属性配置"。 进一步修改配置：

| 配置组 | 配置键 | 说明 |
| -------- | -------- | -------- |
| water     | faas_uri     | 修改为 waterfaas 服务的外网http协议地址（优先用域名）     |
| water     | raas_uri     | 修改为 waterraas 服务的外网http协议地址（优先用域名）     |

修改完成后，可以在 wateradmin 上调试 paas 和 raas 服务。

> 其它一些配置，视情况进行调整。

### 附：补充说明

* water 管理控制台初始账号与密码

> 账号：admin 密码：bcf1234

* water 的访问控制，基于ip安全名单实现（主要给 waterapi 加上）。如果需要，通过启动参数：

> 示例：java -jar waterapi.jar --white=1

* 在使用 water 的服务器上，添加 `waterapi` 域的 host 记录

```yaml
127.0.0.1 waterapi #ip为waterapi服务的地址
```

然后，在应用配置上添加：

```yaml
solon.cloud.water:
  server: "waterapi:9371" #也可以是具体的ip+port（建议用域的方式）
```

* 在linux下建议用配置成service，由 systemctl 命令管理（以 waterapi、wateradmin 为例）

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

* <a name="nginx"> `waterapi` 域的nginx配置示例（注意真实的ip转发）</a>

```ini
upstream waterapi{
    server 127.0.0.1:9370 weight=10;
    server 127.0.0.1:9371 weight=10;
}
server{
    listen 9371;
    server_name waterapi;
    
    location / {
        proxy_pass http://waterapi;
        proxy_set_header  X-Real-IP  $remote_addr;
        proxy_set_header  X-Forwarded-For  $proxy_add_x_forwarded_for;
        proxy_set_header  Host $http_host;
    }
}
```