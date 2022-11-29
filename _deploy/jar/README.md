# Water jar 模式部署说明（简单指引，其它自己搞定）

## 请在完整的看完文档后，再开始动手!!!

## 一、先玩通 docker-compose，对整体有所了解

## 二、基于源码打包 jar

### 1、程序包说明

| 包             | 端口   | 说明                 |
|----------------|-------|--------------------|
| waterapi.jar   | 9371  | 主接口服务              |
| watersev.jar   | 9372  | 批处理服务              |
| wateradmin.jar | 9373  | 管理控制台              |
| waterfaas.jar  | 9374  | FaaS 即时接口服务        |
|                |       |                    |
| xwater.jar     | 19371 | Water助理工具（仅在需要时启用） |

### 2、为 waterapi.jar 准备数据源配置（waterapi_ext / _db.properties）

```properties
water.ds.schema=water
water.ds.server=mysql.water.io:3306
water.ds.username=demo
water.ds.password=demo
```

## 三、部署流程

* 先用 xwater.jar 安装 water；即完成各种配置及初始化
* 启动 waterapi.jar；注意数据源配置
* 等5秒
* 相关服务器，添加 host 记录 (waterapi=x.x.x.x)
* 启动 wateradmin.jar, watersev.jar, waterfaas.jar

