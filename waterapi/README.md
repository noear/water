
## 部署事项:

> 注意：利用 watersetup 先完成 water 的初始化，之后再启动 waterapi 服务


### 一、docker-compose.yml 模式

* 镜像仓库：
    * 中心仓库 `noearorg/waterapi:latest`
    * 阿里仓库 `registry.cn-hangzhou.aliyuncs.com/noearorg/waterapi:latest`
* 镜像端口：`8080`

```yaml
version: '3'

services:
  waterapi:
    image: noearorg/waterapi
    container_name: waterapi
    environment:
      water.ds.schema: water #默认可以不要
      water.ds.server: ${server}
      water.ds.username: ${username}
      water.ds.password: ${password}
    ports:
      - 9371:8080
    networks:
      - water

# JAVA_OPTS: "-Dwater.ds.server=${server} -Dwater.ds.username=${username} -Dwater.ds.password=${password}"
# http://localhost:9371/healthz

```


### 二、jar 模式部署

* 使用镜像：`wateradmin.jar`
* 默认端口：`9371`


**第一步：添加配置文件**

在 waterapi.jar 文件包部添加配置文件：`water_ini/_db.properties`

```properties
water.ds.schema=water
water.ds.server=${server}
water.ds.username=${username}
water.ds.password=${password}
```

**第二步：启动**

```shell
java -jar waterapi.jar
#或者改成 systemctl 控制
systemctl restart waterapi
```





