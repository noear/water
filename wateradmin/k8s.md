
### 生产模式注意事项：
1. 使用镜像：`registry.cn-hangzhou.aliyuncs.com/noearorg/wateradmin:latest`
2. 镜像端口：`8080`


### 安装模式注意事项:

1. 使用镜像：`registry.cn-hangzhou.aliyuncs.com/noearorg/wateradmin:latest`
2. 镜像端口：`8080`
3. 添加环境变量：

```ini
water.setup=1
water.dataSource.schema=water
water.dataSource.server=${server}
water.dataSource.username=${username}
water.dataSource.password=${password}
```

> 利用安装模式，可高定白名单入配置。安装完成后，把容器的环境变量取消掉
 
