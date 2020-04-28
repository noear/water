package org.noear.water.dso;

import org.noear.water.log.Logger;
import org.noear.water.log.WaterLogger;

import java.util.*;

/**
 * 嘿嘿通知接口
 * */
public class HeiheiApi {

    private static final Logger log_heihei = new WaterLogger("water_log_heihei");

    private static final String apiUrl = "https://api.jpush.cn/v3/push";
    private static final String masterSecret = "4a8cd168ca71dabcca306cac";
    private static final String appKey = "af9a9da3c73d23aa30ea4af1";


    /**
     * 嘿嘿通知（经Water服务端处理后再推送）
     *
     * @param target    手机号（多个以,隔开；@alarm 表过报警名单），例：18121212,@alarm
     */
    public String notice(String target, String msg) {
        Map<String, String> params = new HashMap<>();
        params.put("target", target);
        params.put("msg", msg);


        try {
            return CallUtil.post("run/push/", params);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
