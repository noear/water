[![Maven Central](https://img.shields.io/maven-central/v/org.noear/water.client.svg)](https://search.maven.org/search?q=g:org.noear%20AND%20water)

` QQ交流群：22200020 `

# Water for java （水孕育万物...）

为中小量级项目，提供服务开发和治理的整体解决方案

## 开始使用

> 暂时只看看，相关材料未准备好...

#### 配置
* pom.xml / mevan 配置
```xml
<!-- 暂时没有 springboot starter 版本 -->
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>water.client-solon-plugin</artifactId>
    <version>2.0.1-b4</version>
</dependency>
```

* application.yml / 配置
```yaml
water:
  host: "http://water2"           #water服务地址；建议用域名
  service:
    name: "wateradmin"            #当前服务名称
    secretKey: "b5fZK49x71Rnn5Cl" #当前服务密钥，用于消息签名
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
  
          WaterClient.Track.track("water-demo", "path", c.path(), times, node);
      });
  }
}

@XController
class demo{
    @Water("water_log_admin") //日志服务的功能
    WaterLogger log;
    
    @Water("water/water")  //配置服务的功能
    DbContext waterDb;

    @Water          //RPC服务发现的功能
    RockRpc rock;
   
    @XMapping("/")
    public void test(){
        //日志服务：写个日志
        log.info("你好，日志服务"); //(content)
        log.error("demo","test","你好，日志服务"); //(tag,summary,content)
        WaterClient.Log.append("water_log_admin",Level.info,"你好,世界!");//非注解模式
        
        //配置服务：使用配置的数据库上下文进行查询
        var map = waterDb.table("bcf_user").limit(1).select("*").getMap();
        WaterClient.Config.get("water/water").table("bcf_user").select("*").getMap();//非注解模式

        //消息服务：发送消息
        WaterClient.Messsage.sendMessage("test.order.start","{\"order_id\":1}"); //非注解模式
    
        //PaaS服务：调用PaaS接口
        WaterProxy.paas("water/test",null);

        //Rpc发现服务：调用Rpc接口
        AppModel app = rock.getAppById(12);
        XWaterUpstream.xclient(RockRpc.class).getAppById(12); //非注解模式
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