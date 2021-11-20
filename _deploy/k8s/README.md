# Water k8s 模式部署说明

## 请在完整的看完文档后，再开始动手!!!

## 一、环境要求说明（请准备好）

* mysql8：做为主库（字符集：utf8mb4，排序集：utf8mb4_general_ci）
* redis：做为分布式锁、数据临时队列用
* mongodb：做为消息持久化用（也可以使用 mysql8）
* elasticsearch：做为日志持久化用（也可以使用 mysql8 或 mongodb）

## 二、初始化环境

运行 Water 助理服务（在本地或服务器上运行都可）

```shell
docker run -it --rm -p 19371:19371 noearorg/wateraide
```

* 用浏览器打开界面：`http://locahost:19371`，按提示操作

* 完成操作后，关掉服务(有需要再启动，每次用完都关掉)

## 三、开始部署服务

新建域：water

* 添加 water/waterapi 服务 x2+（镜像：noearorg/waterapi:latest）。镜像端口：9371

    ```properties
    #添加环境变量（换成初始化好的 Water DB 配置）：
    water.ds.schema=water
    water.ds.server=mysql.water.io:3306
    water.ds.username=demo
    water.ds.password=123456
    ```

* 添加 water/wateradmin 服务 x1（镜像：noearorg/wateradmin:latest）。镜像端口：8080  //控制台
* 添加 water/waterfaas 服务 x1+（镜像：noearorg/waterfaas:latest）。镜像端口：8080  //即时接口服务
* 添加 water/waterraas 服务 x1+（镜像：noearorg/waterraas:latest）。镜像端口：8080 //不需要，可不部署

* 添加 water/watersev-tol 服务 x1（镜像：noearorg/watersev:latest）。镜像端口：8080 //工具服务，包含： (msgchk,sevchk,syn,mot)

    ```properties
    #添加环境变量：
    water.sss=tol
    ```

* 添加 water/watersev-pln 服务 x1（镜像：noearorg/watersev:latest）。镜像端口：8080 //定时任务调度服务

    ```properties
    #添加环境变量：
    water.sss=pln
    ```


* 添加 water/watersev-msgdis 服务 x1（镜像：noearorg/watersev:latest）。镜像端口：8080 //消息派发服务

    ```properties
    #添加环境变量：
    water.sss=msgdis
    ```

* 添加 water/watersev-msgexg 服务 x1+（镜像：noearorg/watersev:latest）。镜像端口：8080 //消息交换服务

    ```properties
    #添加环境变量：
    water.sss=msgexg
    ```
