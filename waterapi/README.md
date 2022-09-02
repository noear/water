
#### Pod 运行，须添加环境变量。示例：

```properties
#提示：可以把 server 换成 url 属性，配置完整的 jdbcUrl
water.ds.schema=water
water.ds.server=mysql.water.io:3306
water.ds.username=demo
water.ds.password=123456
```


#### Jar 运行，可添加环境变量；或者系统属性；或者扩展配置文件：

waterapi_ext/_db.properties

```properties
#提示：可以把 server 换成 url 属性，配置完整的 jdbcUrl
water.ds.schema=water
water.ds.server=mysql.water.io:3306
water.ds.username=demo
water.ds.password=123456
```
