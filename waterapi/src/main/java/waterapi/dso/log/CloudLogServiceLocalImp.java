package waterapi.dso.log;

import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.model.Instance;
import org.noear.solon.cloud.service.CloudLogService;
import org.noear.solon.logging.event.LogEvent;
import org.noear.water.WW;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.LogHelper;
import waterapi.dso.LogPipelineLocal;

/**
 * @author noear 2021/10/25 created
 */
public class CloudLogServiceLocalImp implements CloudLogService {
    private String loggerNameDefault;

    public CloudLogServiceLocalImp() {
        loggerNameDefault = WW.water_log_api;
    }

    @Override
    public void append(LogEvent logEvent) {
        String loggerName = logEvent.getLoggerName();

        if (Utils.isEmpty(loggerName)) {
            return;
        }

        if (loggerName.contains(".")) {
            loggerName = loggerNameDefault;
        }

        Datetime datetime = Datetime.Now();

        org.noear.water.log.LogEvent log = new org.noear.water.log.LogEvent();

        log.group = Solon.cfg().appGroup();
        log.app_name = Solon.cfg().appName();

        log.logger = loggerName;
        log.level = (logEvent.getLevel().code / 10);
        log.content = LogHelper.contentAsString(logEvent.getContent());

        if (logEvent.getMetainfo() != null) {
            log.tag = logEvent.getMetainfo().get("tag0");
            log.tag1 = logEvent.getMetainfo().get("tag1");
            log.tag2 = logEvent.getMetainfo().get("tag2");
            log.tag3 = logEvent.getMetainfo().get("tag3");
            log.tag4 = logEvent.getMetainfo().get("tag4");
        }

        if(logEvent.getLoggerName().contains(".")){
            log.class_name = logEvent.getLoggerName();
        }

        log.thread_name = Thread.currentThread().getName();
        log.trace_id = CloudClient.trace().getTraceId();
        log.from = Instance.local().address();

        log.log_date = datetime.getDate();
        log.log_fulltime = datetime.getFulltime();

        LogPipelineLocal.singleton().add(log);
    }
}
