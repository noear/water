

#### 四种部署模式：

* docker-compose：基于 docker-compose 模式部署参考
* k8s ：基于 k8s 模式部署参考

* jar_dev ：基于 jar 模式，开发环境部署参考
* jar_pro ：基于 jar 模式，生产环境部署参考



#### 账号与权限管理：（Bcf Studio ）

* Window7+：https://gitee.com/noear/bcf/tree/main/_studio

* 想要使用LDAP登录？

```yaml
# 配置项 water_bcf/bcf.yml ，添加 ldap 连接配置：
bcf.ldap:
  url: "ldap://127.0.0.1:389"
  baseDn: "DC=company,DC=com"
  bindDn: "cn=admin,dc=company,dc=com"
  paasword: "123456"
  userFilter: "cn=%s"
  groupFilter: "cn=%s"
```


#### 告警接收客户端：（嘿嘿）

* iOS：https://testflight.apple.com/join/ZUhQctwS
* Android：https://www.noear.org/app/heihei.apk




