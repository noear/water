
### 两种部署模式：

* docker-compose：基于 docker-compose 模式部署参考
* k8s ：基于 k8s 模式部署参考

### 镜像列表（生产时，tag 须为具体的版本号，切不能用 latest）

| 镜像                      | 镜像端口    | 说明                 |
|-------------------------|-------|--------------------|
| noearorg/waterapi:2.7.2 | 9371  | 主接口服务              |
| noearorg/watersev:2.7.2       | 9372  | 批处理服务              |
| noearorg/wateradmin:2.7.2     | 9373  | 管理控制台              |
| noearorg/waterfaas:2.7.2      | 9374  | FaaS 即时接口服务        |
|                         |       |                    |
| noearorg/xwater:2.7.2         | 19371 | Water助理工具（仅在需要时启用） |



### 环境要求（请准备好）

* mysql 8.x：做为主库（字符集：utf8mb4，排序集：utf8mb4_general_ci）
* redis 5.x+：做为分布式锁、数据临时队列用等
* mongodb 4.x+：做为消息持久化用（也可以使用 mysql8）
* elasticsearch 7.9+：做为日志持久化用（也可以使用 mysql8 或 mongodb）

### 账号与权限管理：

* 想要使用LDAP登录？

```yaml
# 配置项 grit/grit.yml ，添加 ldap 连接配置：
grit.ldap:
  url: "ldap://127.0.0.1"
  baseDn: "DC=company,DC=com"
  bindDn: "cn=admin,dc=company,dc=com"
  paasword: "123456"
  userFilter: "cn=%s"
  groupFilter: "cn=%s"
```


### 助理工具：xwater（用完就停掉）

* 进行 water  环境安装与初始化
* 当 water 无法启动时，可用它修改配置或查看日志


### 告警接收客户端：（嘿嘿）

* iOS：https://testflight.apple.com/join/ZUhQctwS
* Android：http://www.noear.org/app/heihei.apk


### 告警的三种接收方式：

* 默认：嘿嘿客户端
* 钉钉群的 webhook
* 企业群信群的 webhook

    使用 webhook，需添加配置： water/water_heihei
    ```properties
    type=webhook
    url=...
    accessSecret=.. #如果没有，不要加
    ```


