package waterapi.dso.interceptor;

import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import waterapi.dso.LogUtils;

public class LoggingInterceptor implements XHandler {
    @Override
    public void handle(XContext ctx) throws Throwable {
        //记录输入
        //
        LogUtils.info("", ctx);
    }
}
