package org.noear.water.dso;

import org.noear.water.WaterSetting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 白名单接口
 * */
public class WhitelistApi {
    /**
     * 主控组
     * */
    public static final String tag_master = "master";
    /**
     * 客户端组（一般用于检测管理后台客户端）
     * */
    public static final String tag_client = "client";
    /**
     * 服务端组（一般用于检测服务端IP）
     * */
    public static final String tag_server = "server";

    /**
     * 客户端组+服务端组
     * */
    public static final String tag_clientAndServer = "client,server";

    /**
     * 检测，是否为白名单
     *
     * @param tags   分组(多个以,隔开)
     * @param type  类型(ip,mobile,host)
     * @param value 值
     */
    private String checkDo0(String tags, String type, String value) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("tags", tags);
        params.put("type", type);
        params.put("value", value);

        return CallSevUtil.post("/run/whitelist/check/", params);
    }

    private boolean checkDo(String tags, String type, String value) {
        //
        //增加本地缓存
        //
        String cache_key = new StringBuilder(100).append(tags).append("_").append(type).append("_").append(value).toString();
        try {
            String val = WaterSetting.cacheLocal.getBy(10, cache_key, (us) -> checkDo0(tags, type, value));
            return "OK".equals(val);
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean exists(String tags, String type, String value){
        return checkDo(tags,type,value);
    }

    public boolean existsOfIp(String tags,  String value){
        return checkDo(tags,"ip",value);
    }

    public boolean existsOfClientIp(String value){
        return checkDo(tag_client,"ip",value);
    }
    public boolean existsOfClientAndServerIp(String value){
        return checkDo(tag_clientAndServer,"ip",value);
    }
    public boolean existsOfServerIp(String value){
        return checkDo(tag_server,"ip",value);
    }
    public boolean existsOfMasterIp(String value){
        return checkDo(tag_master,"ip",value);
    }


    public boolean existsOfDomain(String tags, String value){
        return checkDo(tags,"domain",value);
    }
    public boolean existsOfMobile(String tags, String value){
        return checkDo(tags,"domain",value);
    }
}
