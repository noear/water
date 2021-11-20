package waterapi.dso.interceptor;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;

public class LoggingInterceptor implements Handler {
    @Override
    public void handle(Context ctx) throws Throwable {
        ctx.attrSet("logging", 1);
    }
}
