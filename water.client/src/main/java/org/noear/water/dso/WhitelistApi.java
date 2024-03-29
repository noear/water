package org.noear.water.dso;

import org.noear.water.WW;
import org.noear.water.WaterAddress;
import org.noear.water.WaterSetting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 白名单服务接口
 *
 * @author noear
 * @since 2.0
 * */
public class WhitelistApi {
    protected final ApiCaller apiCaller;

    public WhitelistApi() {
        apiCaller = new ApiCaller(WaterAddress.getDefApiUrl());
    }

    /**
     * 主控组
     */
    public static final String tag_master = "master";
    /**
     * 客户端组（一般用于检测管理后台客户端）
     */
    public static final String tag_client = "client";
    /**
     * 服务端组（一般用于检测服务端IP）
     */
    public static final String tag_server = "server";

    /**
     * 客户端组+服务端组
     */
    public static final String tag_clientAndServer = "client,server";

    /**
     * 检测，是否为白名单
     *
     * @param tags  分组(多个以,隔开)
     * @param type  类型(ip,mobile,host)
     * @param value 值
     */
    private String checkDo0(String tags, String type, String value) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("tags", tags);
        params.put("type", type);
        params.put("value", value);

        return apiCaller.post("/list/check/", params);
    }

    private boolean checkDo(String tags, String type, String value) {
        //
        //增加本地缓存
        //
        String cache_key = new StringBuilder(100).append(tags).append("_").append(type).append("_").append(value).toString();
        try {
            String val = WaterSetting.cacheLocal.getBy(10, cache_key, (us) -> checkDo0(tags, type, value));
            return "OK".equals(val);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean exists(String tags, String type, String value) {
        return checkDo(tags, type, value);
    }

    public boolean existsOfIp(String tags, String value) {
        return checkDo(tags, WW.whitelist_type_ip, value);
    }

    public boolean existsOfClientIp(String value) {
        return checkDo(tag_client, WW.whitelist_type_ip, value);
    }

    public boolean existsOfClientAndServerIp(String value) {
        return checkDo(tag_clientAndServer, WW.whitelist_type_ip, value);
    }

    public boolean existsOfServerIp(String value) {
        return checkDo(tag_server, WW.whitelist_type_ip, value);
    }

    public boolean existsOfServerToken(String value) {
        return checkDo(tag_server, WW.whitelist_type_token, value);
    }

    public boolean existsOfMasterIp(String value) {
        return checkDo(tag_master, WW.whitelist_type_ip, value);
    }

    public boolean existsOfMasterToken(String value) {
        return checkDo(tag_master, WW.whitelist_type_token, value);
    }

    public boolean existsOfDomain(String tags, String value) {
        return checkDo(tags, WW.whitelist_type_domain, value);
    }

    public boolean existsOfMobile(String tags, String value) {
        return checkDo(tags, WW.whitelist_type_mobile, value);
    }

    public boolean existsOfToken(String tags, String value) {
        return checkDo(tags, WW.whitelist_type_token, value);
    }
}
