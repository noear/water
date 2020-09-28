package org.noear.water.demo_dubbo.consume;

import org.noear.solon.XApp;
import org.noear.solon.core.Aop;
import org.noear.solon.extend.dubbo.EnableDubbo;

@EnableDubbo
public class DemoDubboConsumeApp {
    public static void main(String[] args) {
        XApp.start(DemoDubboConsumeApp.class, args);

        HelloConsume consume = Aop.get(HelloConsume.class);
        System.out.println(consume.home());
    }
}
