

#### 两种部署模式：

* docker-compose：基于 docker-compose 模式部署参考
* k8s ：基于 k8s 模式部署参考


#### 账号与权限管理：

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


#### 告警接收客户端：（嘿嘿）

* iOS：https://testflight.apple.com/join/ZUhQctwS
* Android：https://www.noear.org/app/heihei.apk




