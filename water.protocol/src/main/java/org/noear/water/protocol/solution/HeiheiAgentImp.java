package org.noear.water.protocol.solution;

import org.noear.water.model.ConfigM;
import org.noear.water.model.PropertiesM;
import org.noear.water.protocol.Heihei;
import org.noear.water.protocol.HeiheiAgent;
import org.noear.water.utils.TextUtils;

import java.util.*;

public class HeiheiAgentImp implements HeiheiAgent {
    private Heihei real;

    public HeiheiAgentImp(ConfigM cfg) {
        updateConfig(cfg);
    }


    @Override
    public void updateConfig(ConfigM cfg) {
        if (cfg == null || TextUtils.isEmpty(cfg.value)) {
            real = new HeiheiDefaultImp();
            return;
        }

        PropertiesM props = cfg.getProp();
        String url = props.getProperty("url");
        String accessSecret = props.getProperty("accessSecret");

        if (TextUtils.isNotEmpty(url)) {
            real = new HeiheiWebhookImp(url, accessSecret);
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
