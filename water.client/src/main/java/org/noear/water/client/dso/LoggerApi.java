package org.noear.water.client.dso;

import org.noear.snack.ONode;
import org.noear.water.client.dso.log.Level;
import org.noear.water.tools.ThrowableUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志接口
 * */
public class LoggerApi {
    public static void append(String logger, Level level, Object content) {
        append(logger, level, null, null, null, null, null, content);
    }

    public static void append(String logger, Level level,  String summary, Object content) {
        append(logger, level, null, null, null, null, summary, content);
    }

    public static void append(String logger, Level level, String tag, String summary, Object content) {
        append(logger, level, tag, null, null, null, summary, content);
    }

    public static void append(String logger, Level level, String tag, String tag1, String summary, Object content) {
        append(logger, level, tag, tag1, null, null, summary, content);
    }

    public static void append(String logger, Level level, String tag, String tag1, String tag2, String summary, Object content) {
        append(logger, level, tag, tag1, tag2, null, summary, content);
    }

    public static void append(String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        Map<String, String> params = new HashMap<>();
        params.put("logger", logger);
        params.put("level", String.valueOf(level.code));

        if (tag != null) {
            params.put("tag", tag);
        }

        if (tag1 != null) {
            params.put("tag1", tag1);
        }

        if (tag2 != null) {
            params.put("tag2", tag2);
        }

        if (tag3 != null) {
            params.put("tag3", tag3);
        }

        if (summary != null) {
            params.put("summary", summary);
        }

        if (content != null) {
            if (content instanceof Throwable) {
                //处理异常
                String tmp = ThrowableUtils.getString((Throwable) content);
                params.put("content", tmp);
            } else if (content instanceof String) {
                //处理字符串
                params.put("content", (String) content);
            } else {
                //处理其它对象（进行json）
                String tmp = ONode.load(content).toJson();
                params.put("content", tmp);
            }
        }

        try {
            WaterApi.postAsync("/log/add/", params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
