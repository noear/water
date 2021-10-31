package watersetup;

import org.noear.solon.Utils;
import org.noear.weed.DbContext;

/**
 * @author noear 2021/10/31 created
 */
public class Config {
    static {
        Utils.loadClass("com.mysql.jdbc.Driver");
        Utils.loadClass("com.mysql.cj.jdbc.Driver");
    }

    public static DbContext water;
}
