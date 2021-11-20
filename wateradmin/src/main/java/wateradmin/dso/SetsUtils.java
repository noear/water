package wateradmin.dso;

import org.noear.solon.Solon;

/**
 * @author noear 2021/11/20 created
 */
public class SetsUtils {
    /**
     * 服务数量规模
     *
     * 参数：0上，1中，2大，3超大
     * */
    public static long waterSettingScale() {
        return Solon.cfg().getInt("water.setting.scale", 0);
    }
}
