[![Maven Central](https://img.shields.io/maven-central/v/org.noear/water.client.svg)](https://search.maven.org/search?q=g:org.noear%20AND%20water)

` QQ交流群：22200020 `

# Water for java （水润万物...）

# 使用

#### 配置
* pom.xml / mevan 配置
```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>water.client-solon-plugin</artifactId>
</dependency>
```

* application.yml / 配置
```yaml
water:
  host: "http://water2" 
  service:
    name: "wateradmin"
    secretKey: "b5fZK49x71Rnn5Cl"
```

#### 代码
```java
public class DemoApp{
  public void main(String[] args){
      XApp app = XApp.start(args);

      //监控服务：之：添加接口性能记录
      app.before("**",XMethod.HTTP,-1,(c)->{
          c.attrSet("_timecount", new Timecount().start());
      });
      app.after("**", XMethod.HTTP,(c)->{
          Timecount timecount = c.attr("_timecount", null);
  
          if (timecount == null || c.status() == 404) {
              return;
          }
  
          String node = XWaterAdapter.global().localHost();
          long times = timecount.stop().milliseconds();
  
          WaterClient.Track.track("water-admin", "path", c.path(), times, node);
      });
  }
}

@XController
class demo{
    WaterLogger log = new WaterLogger("water_log_admin");
    
    @Water("water/water") 
    DbContext waterDb;
   
    @XMapping("/")
    public void test(){
        //日志服务：写个日志
        log.info("你好，日志服务");
        log.error("demo","test","你好，日志服务");
        
        //配置服务：使用配置的数据库上下文进行查询
        var map = waterDb.table("bcf_user").limit(1).select("*").getMap();

        //消息服务：发送消息
        WaterClient.Messsage.sendMessage("test.order.start","{\"order_id\":1}");
    
        //PaaS服务：调用PaaS接口
        WaterProxy.paas("water/test",null);
    }
}

//消息服务：订阅消息并处理
@XBean("msg:test.order.end")
public class msg_updatecache implements XMessageHandler {
    @Override
    public boolean handler(MessageM msg) throws Exception {
        //处理消息...
    }
}



```

#### 代码使用

# 管理界面预览
<img src="https://gitee.com/noear/water/raw/master/preview/a0.jpg" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/a1.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/a2.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/a3.png" width="100"/>

<img src="https://gitee.com/noear/water/raw/master/preview/b1.png" width="100"/>

<img src="https://gitee.com/noear/water/raw/master/preview/c1.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/c2.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/c3.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/c4.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/c5.png" width="100"/>

<img src="https://gitee.com/noear/water/raw/master/preview/d2.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/d4.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/d5.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/d6.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/d7.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/d8.png" width="100"/>

<img src="https://gitee.com/noear/water/raw/master/preview/e1.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/e3.png" width="100"/>

<img src="https://gitee.com/noear/water/raw/master/preview/f1.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/f2.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/f5.png" width="100"/>

<img src="https://gitee.com/noear/water/raw/master/preview/h1.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/h2.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/h3.png" width="100"/>
<img src="https://gitee.com/noear/water/raw/master/preview/h4.png" width="100"/>