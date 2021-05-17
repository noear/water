package waterpaas.dso;

import org.noear.solon.core.handle.Context;
import org.noear.water.WW;
import org.noear.water.utils.IPUtils;
import org.noear.water.utils.TextUtils;

/**
 * 来源获取工具（用于链路跟踪）
 *
 * @author noear
 * @since 2.0
 * */
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