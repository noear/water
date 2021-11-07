package demoapi.dso;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.solon.logging.utils.TagsMDC;

/**
 * @author noear 2021/11/7 created
 */

@Slf4j
@Component
public class AppFilterImpl implements Filter {
    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        //1.开始计时（用于计算响应时长）
        long start = System.currentTimeMillis();

        TagsMDC.tag1(ctx.pathNew());

        if ("HEAD".equals(ctx.method()) == false) {
            log.info("> Headers: {}\n> Params: {}", ctx.headerMap(), ctx.paramMap());
        }

        try {
            chain.doFilter(ctx);
        } catch (Throwable e) {
            //2.顺带记录个异常
            log.error("{}", e);
        } finally {
            //3.获得接口响应时长
            long milliseconds = System.currentTimeMillis() - start;
            CloudClient.metric().addMeter(Solon.cfg().appName(), "path", ctx.pathNew(), milliseconds);
        }
    }
}