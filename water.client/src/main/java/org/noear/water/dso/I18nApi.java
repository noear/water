package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.WaterAddress;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author noear 2022/4/7 created
 */
public class I18nApi {
    protected final ApiCaller apiCaller;

    public I18nApi() {
        apiCaller = new ApiCaller(WaterAddress.getCfgApiUrl());
    }

    Map<String, Map> i18nMap = Collections.synchronizedMap(new HashMap());

    /**
     * 获取密钥
     */
    public Map getI18n(String tag, String bundle, String lang) throws IOException {
        String i18nKey = String.format("%s:%s:%s", tag, bundle, lang);

        Map map = i18nMap.get(i18nKey);

        if (map == null) {
            synchronized (i18nKey.intern()) {
                map = i18nMap.get(i18nKey);

                if (map == null) {
                    map = loadDo(tag, bundle, lang);
                }
                i18nMap.put(i18nKey, map);
            }
        }

        return map;
    }

    /**
     * 刷新
     */
    public Map getI18nNoCache(String tag, String bundle, String lang) throws IOException {
        Map map = loadDo(tag, bundle, lang);

        if (map.size() > 0) {
            String i18nKey = String.format("%s:%s:%s", tag, bundle, lang);
            i18nMap.put(i18nKey, map);
        }

        return map;
    }

    protected Map<String, String> loadDo(String tag, String bundle, String lang) throws IOException {
        String json = apiCaller.http("/i18n/get/")
                .data("tag", tag)
                .data("bundle", bundle)
                .data("lang", lang)
                .post();

        ONode oNode = ONode.loadStr(json);

        Map<String, String> map;
        int code = oNode.get("code").getInt();
        if (code == 200) {
            map = oNode.get("data").toObject(Map.class);
        } else {
            map = new LinkedHashMap<>();
        }

        return map;
    }
}
