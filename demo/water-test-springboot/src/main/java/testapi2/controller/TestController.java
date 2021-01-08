package testapi2.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.water.WaterClient;
import testapi2.dso.HelloService;

/**
 * 这是Solon的控制器（基于Solon Bean 容器运行）；可以跳过
 * */
@Controller
public class TestController {
    @Inject
    HelloService helloService;

    @Mapping("/test")
    public String home(String msg) throws Exception {
        helloService.hello();

        if (Utils.isNotEmpty(msg)) {
            WaterClient.Message.sendMessage("test.hello", "test-"+msg);
            return "OK: *" + WaterClient.waterTraceId();
        }else{
            return "NO";
        }
    }
}
