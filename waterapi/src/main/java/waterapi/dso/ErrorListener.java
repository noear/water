package waterapi.dso;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.event.EventListener;
import org.noear.solon.core.handle.Context;

/**
 * 异常监听
 *
 * @author noear 2021/10/31 created
 */
@Component
public class ErrorListener implements EventListener<Throwable> {
    @Override
    public void onEvent(Throwable err) {
        Context ctx = Context.current();

        if (ctx == null) {
            LogUtils.error(ctx, "global", "", "", err);
        } else {
            LogUtils.error(ctx, err);
        }
    }
}
