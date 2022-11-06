package xwater;

import org.noear.solon.Solon;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.util.LogUtil;

public class XwaterApp {
    public static void main(String[] args) {
        NvMap argx = NvMap.from(args);
        argx.put("debug","1");

        Solon.start(XwaterApp.class, argx, x -> {
            //加载环境变量(支持弹性容器设置的环境)
            x.cfg().loadEnv("water.");
        });
    }
}