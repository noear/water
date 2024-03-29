package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.WW;
import org.noear.water.WaterAddress;
import org.noear.water.WaterClient;
import org.noear.water.WaterSetting;
import org.noear.water.model.LogLevel;
import org.noear.water.model.LogM;
import org.noear.water.utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志服务接口
 *
 * @author noear
 * @since 2.0
 * */
public class LogApi {

    protected final ApiCaller apiCaller;

    public LogApi() {
        apiCaller = new ApiCaller(WaterAddress.getLogApiUrl()).asLongHttp();
    }


    /**
     * 添加日志
     */
    @Deprecated
    public void append(String logger, LogLevel level, Map<String, Object> map) {
        if (TextUtils.isEmpty(logger)) {
            return;
        }

        if (logger.indexOf(".") > 0) {
            return;
        }

        LogM log = new LogM();

        log.logger = logger;
        log.level = level.code;

        log.tag = map.getOrDefault("tag", "").toString();
        log.tag1 = map.getOrDefault("tag1", "").toString();
        log.tag2 = map.getOrDefault("tag2", "").toString();
        log.tag3 = map.getOrDefault("tag3", "").toString();
        log.tag4 = map.getOrDefault("tag4", "").toString();

        log.content = LogHelper.contentAsString(map.get("content"));


        append(log);
    }

    public void append(LogM log) {
        if (TextUtils.isEmpty(log.logger)) {
            return;
        }

        if (log.logger.contains(".")) {
            return;
        }

        if (log.level == 0) {
            log.level = LogLevel.ERROR.code;
        }

        if (log.trace_id == null) {
            log.trace_id = WaterClient.waterTraceId();
        }

        if (TextUtils.isEmpty(log.from)) {
            log.from = WaterClient.localServiceHost();
        }

        if (log.log_date == 0) {
            Datetime datetime = Datetime.Now();
            log.log_date = datetime.getDate();
            log.log_fulltime = datetime.getFulltime();
        }

        log.thread_name = Thread.currentThread().getName();

        LogPipeline.singleton().add(log);
    }


    public void appendAll(List<LogM> list, boolean async) {
        if (async) {
            WaterSetting.pools.submit(() -> {
                appendAllDo(list);
            });
        } else {
            appendAllDo(list);
        }
    }

    private void appendAllDo(List<LogM> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        String json = ONode.stringify(list);

        try {
            if (WaterSetting.water_logger_gzip()) {
                apiCaller.postBody("/log/add2/", GzipUtils.gZip(json), WW.mime_glog);
            } else {
                Map<String, String> map = new HashMap<>();
                map.put("list", json);

                apiCaller.post("/log/add2/", map);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}