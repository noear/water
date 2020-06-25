package waterapi.dso;

import org.noear.solon.core.XContext;
import org.noear.water.WCX;
import org.noear.water.utils.TextUtils;

public class FromUtils {
    /**
     * from = service@address:port
     */
    public static String getFrom(XContext ctx) {
        String _from = ctx.header(WCX.http_header_from);
        if (TextUtils.isEmpty(_from)) {
            _from = IPUtils.getIP(ctx);
        }

        return _from;
    }

    public static String getFromName(XContext ctx) {
        String _from = ctx.header(WCX.http_header_from);
        if (TextUtils.isEmpty(_from)) {
            _from = IPUtils.getIP(ctx);
        } else {
            _from = _from.split("@")[0];
        }

        return _from;
    }
}
