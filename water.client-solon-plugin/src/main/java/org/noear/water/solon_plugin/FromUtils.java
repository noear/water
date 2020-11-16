package org.noear.water.solon_plugin;

import org.noear.solon.core.handle.Context;
import org.noear.water.WW;
import org.noear.water.utils.TextUtils;

public class FromUtils {
    /**
     * from = service@address:port
     */
    public static String getFrom(Context ctx) {
        String _from = ctx.header(WW.http_header_from);
        if (TextUtils.isEmpty(_from)) {
            _from = IPUtils.getIP(ctx);
        }

        return _from;
    }

    public static String getFromName(Context ctx) {
        String _from = ctx.header(WW.http_header_from);
        if (TextUtils.isEmpty(_from)) {
            _from = IPUtils.getIP(ctx);
        } else {
            _from = _from.split("@")[0];
        }

        return _from;
    }
}