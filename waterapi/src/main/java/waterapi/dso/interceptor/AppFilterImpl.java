package waterapi.dso.interceptor;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.solon.core.handle.Result;
import org.noear.water.WW;
import org.noear.water.track.TrackBuffer;
import waterapi.Config;
import waterapi.dso.FromUtils;
import waterapi.dso.LogUtils;

/**
 * @author noear 2021/11/20 created
 */
@Component
public class AppFilterImpl implements Filter {
    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        //开始计时
        long start = System.currentTimeMillis();

        try {
            //过滤
            chain.doFilter(ctx);
        } catch (Throwable e) {
            //记录异常
            LogUtils.error(ctx, e);
            ctx.render(Result.failure(e.getLocalizedMessage()));
        } finally {
            long _times = System.currentTimeMillis() - start;

            //记录日志
            if (ctx.attr("logging") != null) {
                LogUtils.info(ctx, _times);
            }

            //记性能
            if (ctx.path().startsWith("_") == false) {

                String _node = Config.getLocalHost();
                String _from = FromUtils.getFrom(ctx);

                TrackBuffer.singleton().append(Config.water_service_name, "cmd", ctx.path(), _times, _node, _from);
            }
        }
    }
}
