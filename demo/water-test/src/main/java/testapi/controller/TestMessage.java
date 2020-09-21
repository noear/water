package testapi.controller;

import org.noear.solon.XUtil;
import org.noear.water.WaterClient;
import org.noear.water.annotation.WaterMessage;
import org.noear.water.dso.MessageHandler;
import org.noear.water.model.MessageM;
import org.noear.water.utils.Datetime;

@WaterMessage("test.hello")
public class TestMessage implements MessageHandler {
    @Override
    public boolean handler(MessageM msg) throws Throwable {
        Datetime dt = Datetime.Now().addSecond(10);
        WaterClient.Message.sendMessage(XUtil.guid(),"test.hello", msg.message, dt.getFulltime());
        return true;
    }
}
