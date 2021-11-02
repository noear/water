package watersetup;

import org.noear.solon.Solon;
import org.noear.solon.core.NvMap;
import watersetup.dso.InitPlugin;

public class WatersetupApp {

    public static void main(String[] args) {
        NvMap argx = NvMap.from(args);

        Solon.start(WatersetupApp.class, argx, x -> {
            //加载环境变量(支持弹性容器设置的环境)
            x.cfg().loadEnv("water.");
            x.enableErrorAutoprint(false);

            x.pluginAdd(-1, new InitPlugin());
        });
    }
}