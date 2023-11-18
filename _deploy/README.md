## 三种部署模式：

* docker-compose：基于 docker-compose 模式部署参考
* k8s ：基于 k8s 模式部署参考
* jar：基于 jar 模式部署参考

## 情况了解

### 1、环境要求（请准备好）

* mysql 8.x：做为主库（字符集：utf8mb4，排序集：utf8mb4_general_ci）
* redis 5.x+：做为分布式锁、数据临时队列用等
* mongodb 4.x+：做为消息持久化用（也可以使用 mysql8）
* elasticsearch 7.9+：做为日志持久化用（也可以使用 mysql8 或 mongodb）


### 2、助理工具 xwater 的作用（用完就停掉）

* 进行 water 环境安装与初始化。相当于是安装工具
* 如果 water 无法启动时，可用它修改配置或查看日志。相当于是安全模式


### 3、了解数据连接配置

进入不同的部署式，会有具体说明

```properties
#配置变量（替换为初始化好的 Water DB 配置；可以把 server 换成 url 属性，配置完整的 jdbcUrl）： 
water.ds.schema=water
water.ds.server=mysql.water.io:3306
#water.ds.url=jdbc:mysql://...
water.ds.username=demo
water.ds.password=demo
```

### 4、想要使用LDAP登录？

```yaml
# 进入 water 管理后台。修改配置项 grit/grit.yml ，添加 ldap 连接配置：
grit.ldap:
  url: "ldap://127.0.0.1"
  baseDn: "DC=company,DC=com"
  bindDn: "cn=admin,dc=company,dc=com"
  paasword: "123456"
  userFilter: "cn=%s"
  groupFilter: "cn=%s"
```

### 5、告警接收客户端（这个很重要）

1) 专属客户端，嘿嘿

* iOS：https://testflight.apple.com/join/ZUhQctwS
* Android：http://www.noear.org/app/heihei.apk
* git: https://gitee.com/noear/heihei

2)  钉钉群 或 企业群信群的 webhook

```properties
# 使用 webhook，需添加配置： water/water_heihei (替代默认的嘿嘿)
type=webhook
url=...
accessSecret=.. #如果没有，不要加
    ```


