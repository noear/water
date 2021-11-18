#### Pod 运行，可加环境变量控制 watersev 的运行功能。示例：

```shell
#watersev-tol 运行模式。环境变量添加：
water.sss=tol

#watersev-pln 运行模式。环境变量添加：
water.sss=msg

#watersev-msgdis 运行模式。环境变量添加：
water.sss=msgdis

#watersev-msgexg 运行模式。环境变量添加：
water.sss=msgexg
```


#### Jar 运行，可加启动参数控制 watersev 的运行功能。示例：

```shell
#watersev-tol 运行模式。环境变量添加：
java -jar watersev.jar -sss=tol

#watersev-pln 运行模式。环境变量添加：
java -jar watersev.jar -sss=pln

#watersev-msgdis 运行模式。环境变量添加：
java -jar watersev.jar -sss=msgdis

#watersev-msgexg 运行模式。环境变量添加：
java -jar watersev.jar -sss=msgexg
```