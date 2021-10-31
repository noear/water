## 部署事项:

> 注意：须在 waterapi 服务部署好之后

### 一、docker-compose.yml 模式部署

* 镜像仓库：
    * 中心仓库 `noearorg/waterraas:latest`
    * 阿里仓库 `registry.cn-hangzhou.aliyuncs.com/noearorg/waterraas:latest`
* 镜像端口：`8080`


```yaml
version: '3'

services:
  waterraas:
    image: noearorg/waterraas:latest
    container_name: waterraas
    ports:
      - 9376:8080
    networks:
      - water

# http://localhost:9376/healthz

```


### 二、jar 模式部署

* 使用镜像：`waterraas.jar`
* 默认端口：`9376`

```shell
java -jar waterraas.jar
#可以改成 systemctl 控制
systemctl restart waterraas
```
