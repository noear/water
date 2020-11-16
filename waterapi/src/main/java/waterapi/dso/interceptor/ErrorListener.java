package waterapi.dso.interceptor;


import org.noear.solon.annotation.Component;
import org.noear.solon.core.event.EventListener;
import org.noear.solon.core.handle.Context;
import waterapi.dso.LogUtils;

@Component
public class ErrorListener implements EventListener<Throwable> {
    @Override
    public void onEvent(Throwable err) {
        Context ctx = Context.current();
        if(ctx == null){
            return;
        }

        LogUtils.error(ctx, err);
    }
}
