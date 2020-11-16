package org.noear.water.provider;

import org.noear.solon.Solon;
import org.noear.solon.extend.dubbo.EnableDubbo;

@EnableDubbo
public class DemoDubboProviderApp {
    public static void main(String[] args) {
        Solon.start(DemoDubboProviderApp.class, args);
    }
}
