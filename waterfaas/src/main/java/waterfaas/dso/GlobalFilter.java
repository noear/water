package waterfaas.dso;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.water.WW;
import org.noear.water.WaterClient;

/**
 * 全局过滤器
 *
 * @author noear 2021/10/31 created
 */
@Component
public class GlobalFilter implements Filter {
    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        //记录开始时间
        long start = System.currentTimeMillis();

        try {
            chain.doFilter(ctx);

            if (ctx.getHandled() == false) {
                ctx.status(404);
            }

        } finally {
            if (ctx.status() != 404) {
                String tag = ctx.attr("file_tag", "global");

                String service = Solon.cfg().appName();
                long _times = System.currentTimeMillis() - start;
                String _node = WaterClient.localHost();
                String _from = CloudClient.trace().getFromId(); //FromUtils.getFrom(c);


                CloudClient.metric().addMeter(service, tag, ctx.path(), _times);
                CloudClient.metric().addMeter(WW.track_service, service, _node, _times);
                CloudClient.metric().addMeter(WW.track_from, service, _from, _times);

//                WaterClient.Track.track(service, tag, ctx.path(), _times);
//                WaterClient.Track.trackNode(service, _node, _times);
//                WaterClient.Track.trackFrom(service, _from, _times);
            }
        }
    }
}
