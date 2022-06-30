package org.noear.water.dso;

import org.noear.redisx.RedisClient;
import org.noear.snack.ONode;
import org.noear.water.WW;
import org.noear.water.WaterAddress;
import org.noear.water.WaterClient;
import org.noear.water.WaterSetting;
import org.noear.water.model.LogM;
import org.noear.water.track.TrackEventGather;
import org.noear.water.track.TrackNames;
import org.noear.water.utils.GzipUtils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.Command;

import java.util.HashMap;
import java.util.Map;

/**
 * 跟踪服务接口
 *
 * @author noear
 * @since 2.0
 * */
public class TrackApi {
    protected final ApiCaller apiCaller;

    public TrackApi() {
        apiCaller = new ApiCaller(WaterAddress.getDefApiUrl());
    }

    //db:5
    public static RedisClient rd_track;
    public static RedisClient rd_track_md5;

    static {
        rd_track = WaterSetting.redis_track_cfg().getRd(5);
        rd_track_md5 = WaterSetting.redis_track_cfg().getRd(6);
        //TrackPipeline.singleton().bind(rd_track);
        TrackNames.singleton().bind(rd_track_md5);
    }

    public String getNameMd5(String name) {
        return TrackNames.singleton().getNameMd5(name);
    }

    public String getName(String nameMd5) {
        return TrackNames.singleton().getName(nameMd5);
    }


    /**
     * 跟踪请求性能
     */
    public void track(String service, String tag, String name, long timespan) {
        String nameMd5 = getNameMd5(name);
        TrackPipeline.singleton().append(service, tag, nameMd5, timespan);
    }

    public void trackNode(String service, String _node, long timespan) {
        if (TextUtils.isNotEmpty(_node)) {
            TrackPipeline.singleton().appendNode(service, _node, timespan);
        }
    }

    public void trackFrom(String service, String _from, long timespan) {
        if (TextUtils.isNotEmpty(_from)) {
            TrackPipeline.singleton().appendFrom(service, _from, timespan);
        }
    }

    /**
     * 跟踪SQL命令性能
     */
    @Deprecated
    public void track(String service, Command cmd, long thresholdValue) {
        trackOfPerformance(service, cmd, thresholdValue);
    }

    /**
     * 跟踪SQL性能
     *
     * @param service        服务名
     * @param thresholdValue 阈值
     */
    public void trackOfPerformance(String service, Command cmd, long thresholdValue) {
        long timespan = cmd.timespan();

        if (timespan > thresholdValue) {
            track0(service, cmd, null, null, null, WaterClient.localHost());
        }
    }


    @Deprecated
    public void track(String service, Command cmd, String ua, String path, String operator, String operator_ip) {
        trackOfBehavior(service, cmd, ua, path, operator, operator_ip);
    }

    /**
     * 跟踪SQL行为
     *
     * @param service     服务名
     * @param ua          ua
     * @param path        请求路径
     * @param operator    操作人
     * @param operator_ip 操作IP
     */
    public void trackOfBehavior(String service, Command cmd, String ua, String path, String operator, String operator_ip) {
        track0(service, cmd, ua, path, operator, operator_ip);
    }

    /**
     * 跟踪SQL命令性能
     */
    private void track0(String service, Command cmd, String ua, String path, String operator, String operator_ip) {
        long interval = cmd.timespan();
        String trace_id = WaterClient.waterTraceId();

        WaterSetting.pools.submit(() -> {
            track0Do(service, trace_id, cmd, interval, ua, path, operator, operator_ip);
        });
    }

    private void track0Do(String service, String trace_id, Command cmd, long interval, String ua, String path, String operator, String operator_ip) {
        int seconds = (int) (interval / 1000);
        String schema = cmd.context.schema();

        String sqlUp = cmd.text.toUpperCase();

        String method = "OTHER";
        if (sqlUp.indexOf("SELECT ") >= 0) {
            method = "SELECT";
        } else if (sqlUp.indexOf("UPDATE ") >= 0) {
            method = "UPDATE";
        } else if (sqlUp.indexOf("DELETE ") >= 0) {
            method = "DELETE";
        } else if (sqlUp.indexOf("INSERT INTO ") >= 0) {
            method = "INSERT";
        }

        LogM logM = new LogM();

        if (path == null) {
            logM.logger = WW.logger_water_log_sql_p;
        } else {
            logM.logger = WW.logger_water_log_sql_b;
        }

        logM.trace_id = trace_id;

        logM.level = 1;

        logM.tag = String.valueOf(seconds);//留空
        logM.tag1 = path; //秒数
        logM.tag2 = operator;
        logM.tag3 = method;
        logM.tag4 = "";

        logM.weight = interval; //=tag5, 毫秒数

        logM.group = schema; //=tag6
        logM.service = service;//=tag7

        StringBuilder content = new StringBuilder();

        content.append(schema).append("::").append(cmd.text);
        content.append("<n-l>$$$").append(ONode.stringify(cmd.paramMap())).append("</n-l>");

        logM.content = content.toString();
        logM.from = operator_ip;

        WaterClient.Log.append(logM);
    }

    public void appendAll(TrackEventGather gather, boolean async) {
        if (async) {
            WaterSetting.pools.submit(() -> {
                appendAllDo(gather);
            });
        } else {
            appendAllDo(gather);
        }
    }

    private void appendAllDo(TrackEventGather gather) {
        if (gather == null) {
            return;
        }

        String json = ONode.stringify(gather);

        try {
            if (WaterSetting.water_logger_gzip()) {
                apiCaller.postBody("/track/add2/", GzipUtils.gZip(json), WW.mime_glog);
            } else {
                Map<String, String> map = new HashMap<>();
                map.put("data", json);

                apiCaller.post("/track/add2/", map);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
