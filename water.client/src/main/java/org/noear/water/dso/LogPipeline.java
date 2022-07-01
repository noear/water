package org.noear.water.dso;

import org.noear.water.WaterClient;
import org.noear.water.model.LogM;
import org.noear.water.utils.EventPipeline;

import java.util.List;

/**
 * 写入时，先写到队列
 * 提交时，每次提交100条；消费完后暂停1秒
 *
 * @author noear
 * @since 2.0
 * */
public class LogPipeline extends EventPipeline<LogM> {
    private static final LogPipeline singleton = new LogPipeline();

    public static LogPipeline singleton() {
        return singleton;
    }

    private LogPipeline() {
        super();
    }

    @Override
    protected void handle(List<LogM> logEvents) {
        WaterClient.Log.appendAll(logEvents, true);
    }
}
