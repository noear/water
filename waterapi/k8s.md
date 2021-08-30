
### 安装模式注意事项:

> 利用 wateradmin 的安装模式，完成相关配置之后再启动 waterapi

1. 使用镜像：`registry.cn-hangzhou.aliyuncs.com/noearorg/waterapi:latest`
2. 镜像端口：`8080`
3. 服务名字：`water`   
4. 添加环境变量：

```ini
water.ds.schema=water
water.ds.server=${server}
water.ds.username=${username}
water.ds.password=${password}
```


