package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.WaterAddress;
import org.noear.water.WaterClient;
import org.noear.water.WaterSetting;
import org.noear.water.track.TrackBuffer;
import org.noear.water.track.TrackNames;
import org.noear.water.utils.RedisX;
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
    public TrackApi(){
        apiCaller = new ApiCaller(WaterAddress.getTrackApiUrl());
    }

    //db:5
    public static RedisX rd_track;
    public static RedisX rd_track_md5;

    static {
        rd_track = WaterSetting.redis_track_cfg().getRd(5);
        rd_track_md5 = WaterSetting.redis_track_cfg().getRd(6);
        TrackBuffer.singleton().bind(rd_track);
        TrackNames.singleton().bind(rd_track_md5);
    }

    public String getNameMd5(String name){
        return TrackNames.singleton().getNameMd5(name);
    }

    public String getName(String nameMd5){
        return TrackNames.singleton().getName(nameMd5);
    }


    /**
     * 跟踪请求性能
     */
    public void track(String service, String tag, String name, long timespan) {
        track(service, tag, name, timespan, null, null);
    }

    /**
     * 跟踪请求性能
     */
    public void track(String service, String tag, String name, long timespan, String _node) {
        track(service, tag, name, timespan, _node, null);
    }

    /**
     * 跟踪请求性能
     *
     * @param service  服务名
     * @param tag      标签
     * @param name     请求名称
     * @param timespan 消耗时间
     * @param _node    节点
     * @param _from    来自哪里
     */
    public void track(String service, String tag, String name, long timespan, String _node, String _from) {
        //
        // 改为直发Redis，节省代理
        //
        String nameMd5 = getNameMd5(name);
        TrackBuffer.singleton().append(service, tag, nameMd5, timespan, _node, _from);
    }


    /**
     * 跟踪SQL命令性能
     */
    public void track(String service, Command cmd, long thresholdValue) {
        long timespan = cmd.timespan();

        if (timespan > thresholdValue) {
            track0(service, cmd, null, null, null, null, null);
        }
    }

    /**
     * 跟踪SQL命令性能
     */
    public void track(String service, Command cmd, String ua, String path, String operator, String operator_ip) {

        track0(service, cmd, ua, path, operator, operator_ip, null);
    }

    /**
     * 跟踪SQL命令性能
     */
    private void track0(String service, Command cmd, String ua, String path, String operator, String operator_ip, String note) {
        long interval = cmd.timespan();
        String trace_id = WaterClient.waterTraceId();

        WaterSetting.pools.submit(() -> {
            track0Do(service, trace_id, cmd, interval, ua, path, operator, operator_ip, note);
        });
    }

    private void track0Do(String service, String trace_id, Command cmd, long interval, String ua, String path, String operator, String operator_ip, String note) {
        Map<String, Object> map = cmd.paramMap();

        Map<String, String> params = new HashMap<>();

        params.put("service", service);
        params.put("schema", cmd.context.schema());
        params.put("interval", String.valueOf(interval));
        params.put("cmd_sql", cmd.text);
        params.put("cmd_arg", ONode.loadObj(map).toJson());

        if (TextUtils.isEmpty(operator) == false) {
            params.put("operator", operator);
        }

        if (TextUtils.isEmpty(operator_ip) == false) {
            params.put("operator_ip", operator_ip);
        }

        if (TextUtils.isEmpty(path) == false) {
            params.put("path", path);
        }

        if (TextUtils.isEmpty(ua) == false) {
            params.put("ua", ua);
        }

        if (TextUtils.isEmpty(note) == false) {
            params.put("note", note);
        }

        try {
            apiCaller.post("/mot/track/sql/", params, trace_id);
        } catch (Exception ex) {

        }
    }
}
