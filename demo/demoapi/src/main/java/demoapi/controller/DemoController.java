package demoapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.annotation.CloudConfig;
import org.noear.solon.cloud.model.Event;
import org.noear.solon.logging.utils.TagsMDC;
import org.noear.weed.DbContext;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author noear 2021/11/7 created
 */
@Slf4j
@Controller
public class DemoController {
    @CloudConfig(name = "demo_db", autoRefreshed = true)  //配置服务的功能（注解模式）
    DbContext demoDb;

    @Mapping("/")
    public String test() throws SQLException {
        if(demoDb == null){
            return "no demo_db config!";
        }

        //日志服务：写个日志
        log.info("你好，日志服务"); //(content)
        TagsMDC.tag0("demo");
        log.error("{}\r\n{}", "test", "你好，日志服务"); //(tag,summary,content)

        //配置服务：使用配置的数据库上下文进行查询
        Map map = demoDb.table("water_reg_service").limit(1).selectMap("*");

        //消息服务：发送消息
        CloudClient.event().publish(new Event("demo.test", "{\"order_id\":1}")); //（非注解模式）

        return "OK";
    }
}