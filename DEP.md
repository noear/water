
# Water 部署说明

## 环境要求说明

* mysql8：做为主库
* memcached：做为缓存使用
* redis：做为分布式锁、ID生成器、数据临时队列用
* mongodb：做为消息持久化用（也可以使用 mysql8）
* jdk11：做为运行时用
* nginx：做为反向代理使用
  
> 建议使用 centos7+ 部署；生产环境最少 4台服务器（接口服务*2、管理服务*1、消息派发*1）；开发环境1台即可；

## 开始部署

### 1、初始化数据库

* water
* water_bcf
* water_log
* water_msg
* water_paas

### 2、进数据库修改链接配置

进入 water 库 water_cfg_properties 表，修改相关连接信息：

```yaml
water/water

water/water_msg
water/water_msg_queue
water/water_msg_store

water/water_log
water/water_log_store

water/water_redis
water/water_cache

water/water_paas

water_bcf/bcf.yml
```

### 3、部署服务

#### （一）生产环境建议方案
**服务器2台（2c4g）**

* waterapi              #接口服务

**服务器1台（2c4g）**

* wateradmin            #管理后台
* waterraas             #RaaS 运行服务（可选部署）
* waterpaas             #PaaS 接口运行服务
* watersev --sss=tool    #工具后台服务

**服务器1台（2c8g）**  

* watersev --sss=pln     #PaaS 定时任务运行服务

**服务器1台（2c8g）** 

* watersev --sss=msg     #消息派发服务（部署4个运行实例）

#### （二）开发环境建议方案

**服务器1台（1c2g）**

* waterapi              #接口服务
* wateradmin            #管理后台
* waterraas             #RaaS 运行服务（可选部署）
* waterpaas             #PaaS 接口运行服务
* watersev              #所有后台服务

### 4、补充说明

* water 的访问控制，基于ip安全名单实理。但部署时，不便于白名单添加。可以通过启动参数关掉：

> --white=0

* water 管理抬台初始密码

> bcf1234

* 使用 nginx 为 waterapi、wateradmin、waterpaas、waterraas 服务添加域解析支持

> 其中 waterapi，必须增加 water 域解析支持

* 在每以使用 water 服务器上，添加 water 域的 host 记录