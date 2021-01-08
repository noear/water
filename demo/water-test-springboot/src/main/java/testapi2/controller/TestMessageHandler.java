package testapi2.controller;

import org.noear.solon.Utils;
import org.noear.water.WaterClient;
import org.noear.water.annotation.WaterMessage;
import org.noear.water.dso.MessageHandler;
import org.noear.water.model.MessageM;
import org.noear.water.utils.Datetime;

/**
 * 消息订阅（可以自己发出去，再自己订阅回来；起来拆解性能消耗的作用）
 *
 * */
@WaterMessage("test.hello")
public class TestMessageHandler implements MessageHandler {
    @Override
    public boolean handler(MessageM msg) throws Throwable {
        Datetime dt = Datetime.Now().addSecond(10);
        WaterClient.Message.sendMessage(Utils.guid(),"test.hello", msg.message, dt.getFulltime());
        return true;
    }
}
