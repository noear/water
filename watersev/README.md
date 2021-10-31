## 部署事项:

> 注意：须在 waterapi 服务部署好之后

### 一、docker-compose.yml 模式部署

* 镜像仓库：
    * 中心仓库 `noearorg/watersev:latest`
    * 阿里仓库 `registry.cn-hangzhou.aliyuncs.com/noearorg/watersev:latest`
* 镜像端口：`8080`


```yaml
version: '3'

services:
  watersev:
    image: noearorg/watersev:latest
    container_name: watersev
    ports:
      - 9372:8080
    networks:
      - water

# http://localhost:9372/healthz

```


### 二、jar 模式部署

* 使用镜像：`watersev.jar`
* 默认端口：`9372`

```shell
java -jar watersev.jar
#可以改成 systemctl 控制
systemctl restart watersev
```



