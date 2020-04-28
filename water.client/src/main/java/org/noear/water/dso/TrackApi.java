package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.utils.TextUtils;
import org.noear.weed.Command;

import java.util.HashMap;
import java.util.Map;

/**
 * 跟踪服务接口
 * */
public class TrackApi {
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
        Map<String, String> params = new HashMap<>();
        if (_node != null) {
            params.put("_node", _node);
        }

        if (_from != null) {
            params.put("_from", _from);
        }

        params.put("service", service);
        params.put("tag", tag);
        params.put("name", name);
        params.put("timespan", timespan + "");

        CallUtil.postAsync("sev/track/api/", params);
    }

    /**
     * 跟踪SQL命令性能
     * */
    public void track(String service, Command cmd, long thresholdValue) {
        long timespan = cmd.timespan();

        if (timespan > thresholdValue) {
            do_track(service, cmd, null, null, null, null, null);
        }
    }

    /**
     * 跟踪SQL命令性能
     * */
    public void track(String service, Command cmd, String ua, String path, String operator, String operator_ip) {

        do_track(service, cmd, ua, path, operator, operator_ip, null);
    }

    /**
     * 跟踪SQL命令性能
     * */
    private void do_track(String service, Command cmd, String ua, String path, String operator, String operator_ip, String note) {
        long interval = cmd.timespan();

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

        CallUtil.postAsync("sev/track/sql/", params);
    }
}
