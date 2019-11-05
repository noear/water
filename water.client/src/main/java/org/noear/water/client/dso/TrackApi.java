package org.noear.water.client.dso;

import org.noear.snack.ONode;
import org.noear.water.tools.StringUtils;
import org.noear.weed.Command;

import java.util.HashMap;
import java.util.Map;

public class TrackApi {
    public static void track(String service, String tag, String name, long timespan) {
        track(service, tag, name, timespan, null, null);
    }

    public static void track(String service, String tag, String name, long timespan, String _node) {
        track(service, tag, name, timespan, _node, null);
    }

    public static void track(String service, String tag, String name, long timespan, String _node, String _from) {
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

        try {
            WaterApi.postAsync("/sev/track/api/",params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void track(String service, Command cmd, long thresholdValue) {
        long timespan = cmd.timespan();

        if (timespan > thresholdValue) {
            do_track(service, cmd, null, null, null, null, null);
        }
    }

    public static void track(String service, Command cmd, String ua, String path, String operator, String operator_ip) {

        do_track(service, cmd, ua, path, operator, operator_ip, null);
    }

    private static void do_track(String service, Command cmd, String ua, String path, String operator, String operator_ip, String note) {
        long interval = cmd.timespan();

        Map<String, Object> map = cmd.paramMap();

        Map<String, String> params = new HashMap<>();

        try {
            params.put("service", service);
            params.put("schema", cmd.context.schema());
            params.put("interval", String.valueOf(interval));
            params.put("cmd_sql", cmd.text);
            params.put("cmd_arg", ONode.loadObj(map).toJson());

            if (StringUtils.isEmpty(operator) == false) {
                params.put("operator", operator);
            }

            if (StringUtils.isEmpty(operator_ip) == false) {
                params.put("operator_ip", operator_ip);
            }

            if (StringUtils.isEmpty(path) == false) {
                params.put("path", path);
            }

            if (StringUtils.isEmpty(ua) == false) {
                params.put("ua", ua);
            }

            if (StringUtils.isEmpty(note) == false) {
                params.put("note", note);
            }

            WaterApi.postAsync("/sev/track/sql/",params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
