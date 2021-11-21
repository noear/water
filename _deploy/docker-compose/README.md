# Water docker-compose 模式部署说明

## 请在完整的看完文档后，再开始动手!!!

## 一、环境要求说明（请准备好）

* mysql8：做为主库（字符集：utf8mb4，排序集：utf8mb4_general_ci）
* redis：做为分布式锁、数据临时队列用
* mongodb：做为消息持久化用（也可以使用 mysql8）
* elasticsearch：做为日志持久化用（也可以使用 mysql8 或 mongodb）

## 二、初始化环境

运行 Water 助理（在本地或服务器上运行都可）

```shell 
docker run -it --rm -p 19371:19371 noearorg/wateraide
```

* 用浏览器打开界面：`http://locahost:19371`，按提示操作

* 完成操作后，关掉服务(有需要再启动，每次用完都关掉)

## 三、开始部署服务

新建个目录：water，把 docker-compose.yml 放进去。然后修改相关的 water.ds. 数据源配置

**进入water目录后，开始运行**

```shell
docker-compose up
```

## 四、后续配置修改

成功进入 wateradmin 管理控制台后，打开 "管理管理 / 属性配置"。 进一步修改配置：

| 配置组 | 配置键 | 说明 |
| -------- | -------- | -------- |
| water     | faas_uri     | 修改为 waterfaas 服务的外网http协议地址（优先用域名）     |
| water     | raas_uri     | 修改为 waterraas 服务的外网http协议地址（优先用域名）     |

修改完成后，重启 wateradmin 服务（之后，就可以在 wateradmin 上调试 paas 和 raas 服务）。

> 其它一些配置，视情况进行调整。
