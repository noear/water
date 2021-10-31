## 部署事项:

> 注意：须在 waterapi 服务部署好之后

### 一、docker-compose.yml 模式部署

* 镜像仓库：
  * 中心仓库 `noearorg/wateradmin:latest` 
  * 阿里仓库 `registry.cn-hangzhou.aliyuncs.com/noearorg/wateradmin:latest`
* 镜像端口：`8080`


```yaml
version: '3'

services:
  wateradmin:
    image: noearorg/wateradmin:latest
    container_name: wateradmin
    ports:
      - 9373:8080
    networks:
      - water

# http://localhost:9373

```


### 二、jar 模式部署

* 使用镜像：`wateradmin.jar`
* 默认端口：`9373`

```shell
java -jar wateradmin.jar
#可以改成 systemctl 控制
systemctl restart wateradmin
```