#### Pod 运行，可加环境变量控制 watersev 的运行功能。示例：

watersev-tool 运行模式。环境变量添加：

```properties
water.sss=tool
```

watersev-pln 运行模式。环境变量添加：

```properties
water.sss=msg
```

watersev-msg 运行模式。环境变量添加：

```properties
water.sss=msg
```


#### Jar 运行，可加启动参数控制 watersev 的运行功能。示例：

watersev-tool 运行模式。环境变量添加：

```properties
java -jar watersev.jar -sss=tool
```

watersev-pln 运行模式。环境变量添加：

```properties
java -jar watersev.jar -sss=pln
```

watersev-msg 运行模式。环境变量添加：

```properties
java -jar watersev.jar -sss=msg
```