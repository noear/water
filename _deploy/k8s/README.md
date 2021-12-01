# Water k8s 模式部署说明

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

* 添加 water 域

* 添加 water/waterapi 服务（镜像：noearorg/waterapi:latest）。主接口
  * 镜像端口：9371
  * 建议2个副本起步
  

  ```properties
  #添加环境变量（替换为初始化好的 Water DB 配置）：
  water.ds.schema=water
  water.ds.server=mysql.water.io:3306
  water.ds.username=demo
  water.ds.password=123456
  ```

* 添加 water/wateradmin 服务（镜像：noearorg/wateradmin:latest）。管理控制台
  * 镜像端口：8080
  * 建议1个副本即可
  * 要配置外网访问地址，建议加域名

  
* 添加 water/waterfaas 服务 （镜像：noearorg/waterfaas:latest）。即时接口服务
  * 镜像端口：8080
  * 建议1个副本起步
  * 要配置外网访问地址，建议加域名

  
* 添加 water/waterraas 服务（镜像：noearorg/waterraas:latest）。规则计算服务
  * 镜像端口：8080
  * 建议1个副本起步`（可选部署）`
  * 要配置外网访问地址，建议加域名


* 添加 water/watersev-tol 服务（镜像：noearorg/watersev:latest）。工具服务，包含： (msgchk,sevchk,syn,mot)
  * 镜像端口：8080
  * 建议1个副本即可


  ```properties
  #添加环境变量：
  water.sss=tol
  ```

* 添加 water/watersev-pln 服务（镜像：noearorg/watersev:latest）。定时任务调度服务
  * 镜像端口：8080
  * 建议1个副本起步（如果定时任务多，2个起步）


  ```properties
  #添加环境变量：
  water.sss=pln
  ```


* 添加 water/watersev-msgdis 服务（镜像：noearorg/watersev:latest）。镜像端口：8080 //消息派发服务
  * 建议副本数为 Msg bus broker 的数量两倍或以上（刚开始可2个起步）


  ```properties
  #添加环境变量：
  water.sss=msgdis
  ```

* 添加 water/watersev-msgexg 服务（镜像：noearorg/watersev:latest）。镜像端口：8080 //消息交换服务
  * 建议副本数与 Msg bus broker 的数量相等（刚开始可1个起步）


  ```properties
  #添加环境变量：
  water.sss=msgexg
  ```


**注：具体可参考yaml配置**

## 四、后续配置修改

进入 wateradmin 管理控制台，打开 "配置管理 / 属性配置"。 进一步修改配置：

| 配置组 | 配置键 | 说明 |
| -------- | -------- | -------- |
| water     | faas_uri     | 修改为 waterfaas 服务的外网http协议地址（优先用域名）     |
| water     | raas_uri     | 修改为 waterraas 服务的外网http协议地址（优先用域名）     |

修改完成后，可以在 wateradmin 上调试 paas 和 raas 服务。

> 其它一些配置，视情况进行调整。

## 附：补充说明

* water 管理控制台初始账号与密码

> 账号：admin 密码：admin

* 如果其它服务跨域调用时，添加环境变量


  ```properties
  #使用 water-solon-plugin 时，配置为：
  solon.cloud.water.server=watreapi.water:9371
  ```
