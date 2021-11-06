package wateraide;

import org.noear.solon.Solon;
import org.noear.solon.core.NvMap;

public class WateraideApp {
    public static void main(String[] args) {
        NvMap argx = NvMap.from(args);
        argx.put("debug","1");

        Solon.start(WateraideApp.class, argx, x -> {
            //加载环境变量(支持弹性容器设置的环境)
            x.cfg().loadEnv("water.");
        });
    }
}