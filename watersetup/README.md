## 部署事项:

### 一、docker-compose.yml 模式

* 镜像仓库：
    * 中心仓库 `noearorg/watersetup:latest`
    * 阿里仓库 `registry.cn-hangzhou.aliyuncs.com/noearorg/watersetup:latest`
* 镜像端口：`8080`

```yaml
version: '3'

services:
  watersetup:
    image: noearorg/watersetup
    container_name: watersetup
    environment:
      water.ds.schema: water #默认可以不要
      water.ds.server: ${server}
      water.ds.username: ${username}
      water.ds.password: ${password}
    ports:
      - 19371:8080
    networks:
      - water

# JAVA_OPTS: "-Dwater.ds.server=${server} -Dwater.ds.username=${username} -Dwater.ds.password=${password}"
# http://localhost:9371/healthz

```


### 二、jar 模式部署

* 使用镜像：`watersetup.jar`
* 默认端口：`19371`


**第一步：添加配置文件**

在 watersetup.jar 文件包部添加配置文件：`water_ini/_db.properties`

```properties
water.ds.schema=water
water.ds.server=${server}
water.ds.username=${username}
water.ds.password=${password}
```

**第二步：启动**

```shell
java -jar watersetup.jar
```





