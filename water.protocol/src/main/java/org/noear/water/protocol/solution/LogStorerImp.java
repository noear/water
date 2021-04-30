package org.noear.water.protocol.solution;

import org.noear.solon.core.event.EventBus;
import org.noear.water.log.Level;
import org.noear.water.log.LogEvent;
import org.noear.water.protocol.LogStorer;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.track.TrackBuffer;
import org.noear.water.utils.SnowflakeUtils;

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
                log.log_id = SnowflakeUtils.genId(); //ProtocolHub.idBuilder.getLogId(log.logger);
            }

            if (log.trace_id == null) {
                log.trace_id = "";
            }

            if (log.thread_name == null) {
                log.thread_name = "";
            }

            if (log.tag == null) {
                log.tag = "";
            } else {
                if (log.tag.length() > 99) {
                    log.tag = log.tag.substring(0, 99);
                }
            }

            if (log.tag1 == null) {
                log.tag1 = "";
            } else {
                if (log.tag1.length() > 99) {
                    log.tag1 = log.tag1.substring(0, 99);
                }
            }

            if (log.tag2 == null) {
                log.tag2 = "";
            } else {
                if (log.tag2.length() > 99) {
                    log.tag2 = log.tag2.substring(0, 99);
                }
            }

            if (log.tag3 == null) {
                log.tag3 = "";
            } else {
                if (log.tag3.length() > 99) {
                    log.tag3 = log.tag3.substring(0, 99);
                }
            }

            if (log.summary == null) {
                log.summary = "";
            } else {
                if (log.summary.length() > 999) {
                    log.summary = log.summary.substring(0, 999);
                }
            }
        }

        Map<String, List<LogEvent>> map = list.stream().collect(Collectors.groupingBy(m -> m.logger));

        for (Map.Entry<String, List<LogEvent>> kv : map.entrySet()) {
            try {
                if (kv.getKey().contains(".")) {
                    EventBus.push(new RuntimeException("Logger *" + kv.getKey() + " is illegal!"));
                    continue;
                }

                ProtocolHub.logSourceFactory.getSource(kv.getKey())
                        .writeAll(kv.getKey(), kv.getValue());

                //track
                int count5 = (int) kv.getValue().stream().filter(m -> m.level == 5).count();
                TrackBuffer.singleton()
                        .appendCount("_waterlog", "logger", kv.getKey(), kv.getValue().size(), 0, 0, count5);

            } catch (Throwable ex) {
                if ("water_log_api".equals(kv.getKey())) {
                    ex.printStackTrace();
                } else {
                    EventBus.push(ex);
                }
            }
        }
    }
}
