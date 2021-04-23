package org.noear.water.protocol.solution;

import org.noear.solon.core.event.EventBus;
import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;
import org.noear.water.protocol.IdBuilder;
import org.noear.water.protocol.LogStorer;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.track.TrackBuffer;
import org.noear.water.utils.TextUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogStorerImp implements LogStorer {

    public LogStorerImp() {
    }

    @Override
    public void write(String logger, String trace_id, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from, Date log_fulltime, String class_name, String thread_name) throws Exception {
        ProtocolHub.logSourceFactory.getSource(logger)
                .write(ProtocolHub.idBuilder.getLogId(logger), logger, trace_id, level, tag, tag1, tag2, tag3, summary, content, from, log_fulltime, class_name, thread_name);
    }

    @Override
    public void writeAll(List<LogEvent> list) {
        for (LogEvent log : list) {
            if (log.log_id == 0) {
                log.log_id = ProtocolHub.idBuilder.getLogId(log.logger);
            }

            if (log.logger != null) {
                if (TextUtils.isEmpty(log.group)) {
                    if (log.logger.length() > 2) {
                        log.group = log.logger.substring(0, 2);
                    }
                }

                if (TextUtils.isNotEmpty(log.group)) {
                    TrackBuffer.singleton().append("waterlog", log.group, log.logger, 1);
                }
            }
        }

        Map<String, List<LogEvent>> map = list.stream().collect(Collectors.groupingBy(m -> m.logger));

        for (Map.Entry<String, List<LogEvent>> kv : map.entrySet()) {
            try {
                ProtocolHub.logSourceFactory.getSource(kv.getKey())
                        .writeAll(kv.getKey(), kv.getValue());

            } catch (Throwable ex) {
                EventBus.push(ex);
            }
        }
    }
}
