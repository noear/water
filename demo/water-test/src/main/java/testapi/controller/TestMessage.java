package testapi.controller;

import org.noear.water.WaterClient;
import org.noear.water.annotation.WaterMessage;
import org.noear.water.dso.MessageHandler;
import org.noear.water.model.MessageM;

@WaterMessage("test.hello")
public class TestMessage implements MessageHandler {
    @Override
    public boolean handler(MessageM msg) throws Throwable {
        WaterClient.Message.sendMessage("test.hello", "Hello");
        return true;
    }
}
