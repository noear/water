package org.noear.water.protocol.solution;

import org.noear.water.model.ConfigM;
import org.noear.water.model.PropertiesM;
import org.noear.water.protocol.Heihei;
import org.noear.water.utils.TextUtils;

import java.util.*;

public class HeiheiImp implements Heihei {
    private final Heihei real;

    public HeiheiImp(ConfigM cfg) {
        if (cfg == null || TextUtils.isEmpty(cfg.value)) {
            real = new HeiheiDefaultImp();
            return;
        }

        PropertiesM props = cfg.getProp();
        String type = props.getProperty("type");
        String url = props.getProperty("url");
        String accessSecret = props.getProperty("accessSecret");

        if ("dingding".equals(type)) {
            real = new HeiheiDingdingImp(url, accessSecret);
        } else {
            real = new HeiheiDefaultImp();
        }
    }

    @Override
    public String push(String tag, Collection<String> alias, String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }

        return real.push(tag, alias, content);
    }
}
