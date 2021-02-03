package waterapi.dso;

import org.noear.solon.core.event.EventBus;
import org.noear.water.log.LogEvent;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.EventPipeline;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 写入时，先写到队列
 * 提交时，每次提交100条；消费完后暂停1秒
 *
 * */
public class LogPipelineLocal extends EventPipeline<LogEvent> {
    private static LogPipelineLocal singleton = new LogPipelineLocal();

    public static LogPipelineLocal singleton() {
        return singleton;
    }

    private LogPipelineLocal() {
        super();
    }

    @Override
    protected void handler(List<LogEvent> logEvents) {
        ProtocolHub.logStorer.writeAll(logEvents);
    }
}