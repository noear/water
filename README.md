[![Maven Central](https://img.shields.io/maven-central/v/org.noear/water.client.svg)](https://search.maven.org/search?q=g:org.noear%20AND%20water)

` QQ交流群：22200020 `

# Water for java （水孕育万物...）

为中小量级项目，提供服务开发和治理的整体解决方案（可以理解为微服务架构套件）。本案基于 Solon 框架开发，并支持完整的 Solon Cloud 规范；已在生产环境跑了4年。



## 开始

### 了解框架与构件

| 组件 | 说明 |
| --- | --- |
| org.noear:water.client | 框架：Water 客户端 |
| org.noear:water-solon-plugin | 框架：Water 客户端 for solon（也可用于 Spring Boot 项目） |
| | |
| org.noear:waterapi | 构建：Water 服务端 |
| org.noear:watersev | 构建：Water 后台服务（消息派发；定时任务；服务检测...） |
| org.noear:wateradmin | 构建：Water 控制台 |
| org.noear:waterpaas | 构建：Water FaaS 服务，提供轻量级FaaS服务 |
| org.noear:waterraas | 构建：Water RaaS 服务，提供轻量级规则计算服务 |



### 控制台演示站

地址： [http://water.noear.org](http://water.noear.org)  （账号：demo ；密码：demo ）


关键持久化说明：

* 日志持久化，支持：MySql、PostgreSQL、MongoDb、ElasticSearch、ClickHouse
* 消息持久化，支持：MySql、PostgreSQL、MongoDb

### (一) 使用

#### 配置
* pom.xml / mevan 配置
```xml
<!-- 客户端版本 -->
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>water.client</artifactId>
    <version>2.3.0</version>
</dependency>

<!-- solon cloud 集成版本 （也可用于 Spring Boot 项目） -->
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>water-solon-plugin</artifactId>
    <version>1.5.54</version>
</dependency>
```

* application.yml / 配置说明
```yml
solon.app:
  name: "wateradmin"
  group: "water"

solon.cloud.water:
  server: "water"                   #WATER服务地址
  config:
    load: "test.properties"         #默认加载的配置
  event:
    receive: "api.water.io"         #当前服务主机地址（一般设为外网地址；用于订阅业务消息用）
    #receive: "@water_admin_host"   #如果域名会变，可通过配置指定（@开头，表示从water配置服务获取）
  log:
    default: "water_log_admin"      #默认日志记录器
```

#### 代码
```java
public class DemoApp {
    public void main(String[] args) {
        SolonApp app = Solon.start(DemoApp.class, args);

        //监控服务：之：添加接口性能记录（一般这个过滤器写成独立类）
        Logger log = LoggerFactory.getLogger(DemoApp.class);
        app.filter((ctx, chain) -> {
            //1.开始计时（用于计算响应时长）
            long start = System.currentTimeMillis();

            try {
                chain.doFilter(ctx);
            } catch (Throwable e) {
                //2.顺带记录个异常
                log.error(e);
            } finally {
                //3.获得接口响应时长
                long milliseconds = System.currentTimeMillis() - start;
                CloudClient.metric().addMeter("path", c.pathNew(), milliseconds);
            }
        });
    }
}

@Slf4j
@Controller
class demo{
    @CloudConfig(value = "water", autoRefreshed = true)  //配置服务的功能（注解模式）
    DbContext waterDb;

    @NamiClient            //RPC服务发现的功能（注解模式）
    RockService rockService;
   
    @Mapping("/")
    public void test(){
        //日志服务：写个日志
        log.info("你好，日志服务"); //(content)
        TagsMDC.tag0("demo");
        log.error("{}\r\n{}","test","你好，日志服务"); //(tag,summary,content)
        
        //配置服务：使用配置的数据库上下文进行查询
        var map = waterDb.table("bcf_user").limit(1).select("*").getMap();

        //消息服务：发送消息
        CloudClient.event().publish(new Event("test.order.start", "{\"order_id\":1}")); //（非注解模式）

        //Rpc发现服务：调用Rpc接口
        AppModel app = rockService.getAppById(12);
    }
}

//消息订阅：订阅消息并处理（根据：topic 进行订阅）
@CloudEvent("test.order.end")
public class msg_updatecache implements CloudEventHandler {
    @Override
    public boolean handler(Event event) throws Exception {
        //处理消息...
        return true;
    }
}

//配置订阅：关注配置的实时更新
@CloudConfig("water")
public class TestConfigHandler implements CloudConfigHandler {
    @Override
    public void handler(Config config) {

    }
}

//分布式任务
@CloudJob(name = "water_test_jost", cron7x = "0 1 * * * ?")
public class TestJobHandler implements Handler {
    @Override
    public void handler(Context ctx) {
        //处理任务...
    }
}


```

### （二）代码示例

[https://gitee.com/noear/water_demo](https://gitee.com/noear/water_demo)

### （三）部署

参考《DEP.md》

### （四）架构说明

> 相关材料未准备好...

# 管理界面预览

* 报警专用客户端
<img src="https://gitee.com/noear/water/raw/master/preview/a0.jpg" height="300"/>

* 管理小工具
<img src="https://gitee.com/noear/water/raw/master/preview/a1.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/a2.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/a3.png" height="300"/>

* 日志查询
<img src="https://gitee.com/noear/water/raw/master/preview/b1.png" height="300"/>

* 消息中心管理
<img src="https://gitee.com/noear/water/raw/master/preview/c1.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/c2.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/c3.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/c4.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/c5.png" height="300"/>

* 服务监控
<img src="https://gitee.com/noear/water/raw/master/preview/d2.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/d4.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/d5.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/d6.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/d7.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/d7x.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/d8.png" height="300"/>

* 配置管理
<img src="https://gitee.com/noear/water/raw/master/preview/e1.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/e3.png" height="300"/>

* PaaS管理后台
<img src="https://gitee.com/noear/water/raw/master/preview/f1.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/f2.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/f5.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/fx.png" height="300"/>

* 开发者小工具
<img src="https://gitee.com/noear/water/raw/master/preview/h1.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/h2.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/h3.png" height="300"/>
<img src="https://gitee.com/noear/water/raw/master/preview/h4.png" height="300"/>