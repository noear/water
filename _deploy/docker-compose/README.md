# Water docker-compose 模式部署说明

## 请在完整的看完文档后，再开始动手!!!

## 一、环境要求说明（请准备好）

* mysql 8.x：做为主库（字符集：utf8mb4，排序集：utf8mb4_general_ci）
* redis 5.x+：做为分布式锁、数据临时队列用
* mongodb 4.x+：做为消息持久化用（也可以使用 mysql8）
* elasticsearch 7.9+：做为日志持久化用（也可以使用 mysql8 或 mongodb）

## 二、初始化环境

运行 Water 助理工具（在本地或服务器上运行都可）

```shell 
docker run -it --rm -p 19371:19371 noearorg/xwater:2.8.2
```

* 用浏览器打开界面：`http://locahost:19371`，按提示操作

* 完成操作后，关掉服务(有需要再启动，每次用完都关掉)

## 三、开始部署服务

新建个目录：water，把 docker-compose.yml 放进去。然后修改相关的 water.ds. 数据源配置

**进入 water 目录后，开始运行**

```shell
docker-compose up
```

**注意：**

* 如果修改了 docker-compose.yml 配置，引入 networks，要把每个服务的ip固定下来

## 四、后续配置修改

进入 wateradmin 管理控制台，打开 "配置管理 / 属性配置"。 进一步修改配置：

| 配置组 | 配置键 | 说明 |
| -------- | -------- | -------- |
| water     | faas_uri     | 修改为 waterfaas 服务的外网http协议地址（优先用域名）     |

修改完成后，可以在 wateradmin 上调试 paas 服务（即"函数计算"）。

> 其它一些配置，视情况进行调整。

## 五、客户端使用

* 使用 water-solon-plugin 组件，并配置为：solon.cloud.water.server=waterapi:9371
* 服务所在机器，需添加 waterapi 的 host 配置
* 具体代码演示，可参考 demo/demoapi 项目

> 基本套路是：让 waterapi 运行正常，然后让其它服务都连接它


## 附：补充说明

* water 管理控制台初始账号与密码

> 账号：admin 密码：admin

* 在使用 water 的服务器上，添加 `waterapi` 域的 host 记录

```yaml
127.0.0.1 waterapi #ip为waterapi服务的地址
```

然后，在应用配置上添加：

```yaml
solon.cloud.water:
  server: "waterapi:9371" #也可以是具体的ip+port（建议用域的方式）
```

