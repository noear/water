package org.noear.water.dso;

import java.util.HashMap;
import java.util.Map;

/**
 * 白名单接口
 * */
public class WhitelistApi {
    /**
     * 检测，是否为白名单
     *
     * @param tags   分组(多个以,隔开)
     * @param type  类型(ip,mobile,host)
     * @param value 值
     */
    private boolean checkDo(String tags, String type, String value) {
        Map<String, String> params = new HashMap<>();
        params.put("tags", tags);
        params.put("type", type);
        params.put("value", value);

        try {
            return "OK".equals(CallUtil.post("run/whitelist/check/", params));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean existsOfIp(String tags,  String value){
        return checkDo(tags,"ip",value);
    }

    public boolean existsOfDomain(String tags, String value){
        return checkDo(tags,"domain",value);
    }

    public boolean existsOfMobile(String tags, String value){
        return checkDo(tags,"domain",value);
    }
}
