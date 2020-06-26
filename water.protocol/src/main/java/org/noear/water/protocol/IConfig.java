package org.noear.water.protocol;

import org.noear.water.model.ConfigM;

public interface IConfig {
    default ConfigM getByTagKey(String tagKey) {
        String[] ss = tagKey.split("/");
        return get(ss[0], ss[1]);
    }


    /**
     * 获取配置（不会返回null）
     */
    ConfigM get(String tag, String key);
}
