
#### Pod 运行，须添加环境变量。示例：

```properties
water.ds.schema=water
water.ds.server=mysql.water.io:3306
water.ds.username=demo
water.ds.password=123456
```

#### Jar 运行，可添加环境变量；或者系统属性；或者扩展配置文件：

waterapi_ext/_db.properties

```properties
water.ds.schema=water
water.ds.server=mysql.water.io:3306
water.ds.username=demo
water.ds.password=123456
```
