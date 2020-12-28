package testapi2.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.water.WaterClient;

@Controller
public class TestController {
    @Mapping("/")
    public String home(String msg) throws Exception {
        if (Utils.isNotEmpty(msg)) {
            WaterClient.Message.sendMessage("test.hello", "test-"+msg);
            return "OK: *" + WaterClient.waterTraceId();
        }else{
            return "NO";
        }
    }
}
