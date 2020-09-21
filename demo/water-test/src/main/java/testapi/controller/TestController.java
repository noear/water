package testapi.controller;

import org.noear.solon.XUtil;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.water.WaterClient;


@XController
public class TestController {
    @XMapping("/")
    public String home(String msg) throws Exception{
        if(XUtil.isNotEmpty(msg)){
            WaterClient.Message.sendMessage("water.test.hello", msg);
        }
        return "OK";
    }
}
