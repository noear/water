package org.noear.water.admin.plugin_aliyun;

import org.noear.solon.XApp;
import org.noear.solon.core.XPlugin;
import org.noear.weed.DbContext;

public class XPluginImp implements XPlugin {
    @Override
    public void start(XApp app) {
        app.sharedGet("db", (val) -> {
            Config.db = (DbContext) val;
        });
    }
}
