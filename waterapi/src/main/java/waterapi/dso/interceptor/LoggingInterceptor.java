package waterapi.dso.interceptor;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import waterapi.dso.LogUtils;

public class LoggingInterceptor implements Handler {
    @Override
    public void handle(Context ctx) throws Throwable {
        //记录输入
        //
        try {
            LogUtils.info("", ctx);
        } catch (Throwable ex) {

        }
    }
}
