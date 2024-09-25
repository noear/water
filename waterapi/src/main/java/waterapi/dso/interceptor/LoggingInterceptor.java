package waterapi.dso.interceptor;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.solon.core.handle.Handler;

public class LoggingInterceptor implements Filter {

    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        ctx.attrSet("logging", 1);
        chain.doFilter(ctx);
    }
}
