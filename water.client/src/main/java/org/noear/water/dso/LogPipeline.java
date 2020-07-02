package org.noear.water.dso;

import org.noear.water.WaterClient;
import org.noear.water.log.LogEvent;
import org.noear.water.utils.EventPipeline;

/**
 * 写入时，先写到队列
 * 提交时，每次提交100条；消费完后暂停1秒
 *
 * */
public class LogPipeline extends EventPipeline<LogEvent> {
    private static LogPipeline singleton = new LogPipeline();

    public static LogPipeline singleton() {
        return singleton;
    }

    private LogPipeline() {
        super((list) -> {
            WaterClient.Log.appendAll(list, true);
        });
    }
}
