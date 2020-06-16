package org.noear.water.solon_plugin;

import org.noear.solon.core.XContext;
import org.noear.water.utils.TextUtils;

public class FromUtils {
    /**
     * _from = service@address:port
     * */
    public static String _from(XContext ctx){
        String _from = ctx.header("_from");
        if (TextUtils.isEmpty(_from)) {
            _from = IPUtils.getIP(ctx);
        }

        return _from;
    }

    public static String _fromService(XContext ctx){
        String _from = ctx.header("_from");
        if (TextUtils.isEmpty(_from)) {
            _from = IPUtils.getIP(ctx);
        } else {
            _from = _from.split("@")[0];
        }

        return _from;
    }
}
