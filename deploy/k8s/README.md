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
docker run -it --rm -p 19371:8080 noearorg/wateraide
```

* 用浏览器打开界面：`http://locahost:19371`，按提示操作

* 完成操作后，关掉服务(有需要再启动，每次用完都关掉)

## 三、开始部署服务

新建域：water

* 添加 water/waterapi 服务 x2+（镜像：noearorg/waterapi:latest）

    ```properties
    #添加环境变量（换成初始化好的 Water DB 配置）：
    water.ds.schema=water
    water.ds.server=mysql.water.io:3306
    water.ds.username=demo
    water.ds.password=123456
    ```

* 添加 water/wateradmin 服务 x1（镜像：noearorg/wateradmin:latest）
* 添加 water/waterpaas 服务 x1+（镜像：noearorg/waterpaas:latest）
* 添加 water/waterraas 服务 x1+（镜像：noearorg/waterraas:latest）；这个不需要，可以不部署
* 添加 water/watersev-tool 服务 x1（镜像：noearorg/watersev:latest）；更新时，新停再启

    ```properties
    #添加环境变量：
    water.sss=tool
    ```

* 添加 water/watersev-pln 服务 x1（镜像：noearorg/watersev:latest）；更新时，新停再启

    ```properties
    #添加环境变量：
    water.sss=pln
    ```

* 添加 water/watersev-msg 服务 x1+（镜像：noearorg/watersev:latest）

    ```properties
    #添加环境变量：
    water.sss=msg
    ```
