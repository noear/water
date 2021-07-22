
### 安装模式注意事项:

> waterapi 部署好之后，再部署 

1. 使用镜像：`registry.cn-hangzhou.aliyuncs.com/noearorg/watersev:latest`
2. 镜像端口：`8080`
*. 添加环境变量：
```ini
# 或者不加，表示运行 watresev 里的所有服务；或者加 tool、pln、msg ，表示启动特定子服务
water.sss=
```   


