package org.noear.water.protocol.solution;

import org.noear.solon.Utils;
import org.noear.solon.core.event.EventBus;
import org.noear.water.model.LogM;
import org.noear.water.protocol.LogStorer;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.utils.SnowflakeUtils;
import org.noear.water.track.TrackBuffer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogStorerImp implements LogStorer {

    public LogStorerImp() {
    }


    @Override
    public void writeAll(List<LogM> list) {
        if(list == null){
            return;
        }

        if(ProtocolHub.logSourceFactory == null){
            return;
        }

        for (LogM log : list) {
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

            if (log.tag4 == null) {
                log.tag4 = "";
            } else {
                if (log.tag4.length() > 99) {
                    log.tag4 = log.tag4.substring(0, 99);
                }
            }

            if (Utils.isEmpty(log.tag4) && Utils.isNotEmpty(log.class_name)) {
                if (log.class_name.contains(".")) {
                    log.tag4 = log.class_name.substring(log.class_name.lastIndexOf("."));
                }
            }
        }

        Map<String, List<LogM>> map = list.stream()
                .collect(Collectors.groupingBy(m -> m.logger));

        for (Map.Entry<String, List<LogM>> kv : map.entrySet()) {
            try {
                if (kv.getKey().contains(".")) {
                    EventBus.publish(new RuntimeException("Logger *" + kv.getKey() + " is illegal!"));
                    continue;
                }

                ProtocolHub.getLogSource(kv.getKey())
                        .writeAll(kv.getKey(), kv.getValue());

                //track
                int count5 = (int) kv.getValue().stream().filter(m -> m.level == 5).count();
                TrackBuffer.singleton()
                        .appendCount("_waterlog", "logger", kv.getKey(), kv.getValue().size(), 0, 0, count5);

            } catch (Throwable ex) {
                if ("water_log_api".equals(kv.getKey())) {
                    ex.printStackTrace();
                } else {
                    EventBus.publish(ex);
                }
            }
        }
    }
}
