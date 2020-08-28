package org.noear.water.demo_dubbo.consume;

import org.noear.solon.XApp;
import org.noear.solon.extend.dubbo.EnableDubbo;
import org.noear.water.utils.HttpUtils;

@EnableDubbo
public class DemoDubboConsumeApp {
    public static void main(String[] args) {
        XApp.start(DemoDubboConsumeApp.class, args);

//        try {
//            HttpUtils.http("http://localhost:8080/").get();
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    }
}
