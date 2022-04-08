package demoapi.controller.event;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.cloud.CloudEventHandler;
import org.noear.solon.cloud.annotation.CloudEvent;
import org.noear.solon.cloud.model.Event;
import org.noear.solon.logging.utils.TagsMDC;

/**
 * @author noear 2021/11/7 created
 */

//消息订阅：订阅消息并处理（根据：topic 进行订阅）
@Slf4j
@CloudEvent("demo.test")
public class Event_demo_test implements CloudEventHandler {
    @Override
    public boolean handle(Event event) throws Exception {
        //处理消息...
        TagsMDC.tag0("msg");

        log.info("我收到消息：" + event.content());
        return true;
    }
}