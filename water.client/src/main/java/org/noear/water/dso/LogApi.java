package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.WaterClient;
import org.noear.water.log.Level;
import org.noear.water.log.WaterLogger;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ThrowableUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志服务接口
 * */
public class LogApi {
    private  Map<String,WaterLogger> loggerMap = new ConcurrentHashMap<>();
    public  WaterLogger logger(String logger) {
        WaterLogger tmp = loggerMap.get(logger);
        if (tmp == null) {
            tmp = new WaterLogger(logger);
            WaterLogger l = loggerMap.putIfAbsent(logger, tmp);
            if (l != null) {
                tmp = l;
            }
        }

        return tmp;
    }

    /**
     * 添加日志
     * */
    public void append(String logger, Level level, Map<String, Object> map) {
        append(logger, level,
                (String) map.get("tag"),
                (String) map.get("tag1"),
                (String) map.get("tag2"),
                (String) map.get("tag3"),
                (String) map.get("summary"),
                map.get("content"));
    }

    /**
     * 添加日志
     * */
    public void append(String logger, Level level,  String summary, Object content) {
        append(logger, level, null, null, null, null, summary, content, true);
    }

    /**
     * 添加日志
     * */
    public void append(String logger, Level level, String tag,  String summary, Object content) {
        append(logger, level, tag, null, null, null, summary, content, true);
    }

    /**
     * 添加日志
     * */
    public void append(String logger, Level level, String tag, String tag1, String summary, Object content) {
        append(logger, level, tag, tag1, null, null, summary, content, true);
    }

    /**
     * 添加日志
     * */
    public void append(String logger, Level level, String tag, String tag1, String tag2, String summary, Object content) {
        append(logger, level, tag, tag1, tag2, null, summary, content, true);
    }

    /**
     * 添加日志
     * */
    public void append(String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content) {
        append(logger, level, tag, tag1, tag2, tag3, summary, content, true);
    }

    /**
     * 添加日志
     *
     * @param logger  日志接收器
     * @param level   等级
     * @param tag     标签
     * @param tag1    标签1
     * @param tag2    标签2
     * @param tag3    标签3
     * @param summary 简介
     * @param content 内容
     * @param async   是否异步提交
     */
    public void append(String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, boolean async) {
        if(TextUtils.isEmpty(logger)){
            return;
        }

        if(logger.indexOf(".") > 0){
            return;
        }

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

        if (WaterClient.serviceAddr() != null) {
            params.put("from", WaterClient.serviceAddr());
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
            if (async) {
                CallUtil.postAsync("log/add/", params);
            } else {
                CallUtil.post("log/add/", params);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
