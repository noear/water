package org.noear.water.admin.tools.dso;


import org.noear.solon.core.XContext;
import org.noear.water.admin.tools.utils.IPUtil;

/**
 * Created by yuety on 14-9-14.
 */
public final class Session {
    public static final XContext getContext() {
        return XContext.current();
    }

    public static final String getUA() {
        return getContext().header("User-Agent");
    }

    //获取管理后台的真实路径 xyj,20180928
    public static final String getPath() {
        return getContext().path();
    }

    public static final String getIP() {
        return IPUtil.getIP(getContext());
    }

    //
    // ...
    //

    private static final SessionBcf _current = new SessionBcf();

    public static SessionBcf current() {
        return _current;
    }
}
