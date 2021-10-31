
## 部署事项:

> 注意：须在 waterapi 服务部署好之后

### 一、docker-compose.yml 模式部署

* 镜像仓库：
    * 中心仓库 `noearorg/waterpaas:latest`
    * 阿里仓库 `registry.cn-hangzhou.aliyuncs.com/noearorg/waterpaas:latest`
* 镜像端口：`8080`


```yaml
version: '3'

services:
  waterpaas:
    image: noearorg/waterpaas:latest
    container_name: waterpaas
    ports:
      - 9374:8080
    networks:
      - water

# http://localhost:9374/healthz

```


### 二、jar 模式部署

* 使用镜像：`waterpaas.jar`
* 默认端口：`9374`

```shell
java -jar waterpaas.jar
#可以改成 systemctl 控制
systemctl restart waterpaas
```

