package waterapi.dso.interceptor;

import org.noear.solon.core.aspect.Interceptor;
import org.noear.solon.core.aspect.Invocation;
import org.noear.solon.core.handle.Context;
import waterapi.dso.LogUtils;

public class LoggingInterceptor implements Interceptor {
    @Override
    public Object doIntercept(Invocation inv) throws Throwable {
        Context ctx = Context.current();

        try{
            Object tmp = inv.invoke();
            ctx.result = tmp;
            LogUtils.info(ctx, null);

            return tmp;
        }catch (Exception e){
            LogUtils.info(ctx, e);
            throw e;
        }
    }
}
