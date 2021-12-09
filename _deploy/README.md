

### 两种部署模式：

* docker-compose：基于 docker-compose 模式部署参考
* k8s ：基于 k8s 模式部署参考


### 镜像列表

| 镜像    | 端口    | 说明          |
|-------|-------|-------------|
| noearorg/waterapi | 9371  | 主接口服务       |
| noearorg/watersev | 9372  | 批处理服务       |
| noearorg/wateradmin | 9373  | 管理控制台       |
| noearorg/waterfaas | 9374  | FaaS 即时接口服务 |
| noearorg/waterraas | 9375  | 规则计算服务      |
|  |       |             |
| noearorg/xwater | 19371 | Water助理工具   |

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


### 告警接收客户端：（嘿嘿）

* iOS：https://testflight.apple.com/join/ZUhQctwS
* Android：https://www.noear.org/app/heihei.apk




