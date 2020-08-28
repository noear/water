package org.noear.water.provider;

import org.noear.solon.XApp;
import org.noear.solon.extend.dubbo.EnableDubbo;

@EnableDubbo
public class DemoDubboProviderApp {
    public static void main(String[] args) {
        XApp.start(DemoDubboProviderApp.class, args);
    }
}
