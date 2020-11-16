package waterapi.dso.interceptor;


import org.noear.solon.annotation.XBean;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.XEventListener;
import waterapi.dso.LogUtils;

@XBean
public class ErrorListener implements XEventListener<Throwable> {
    @Override
    public void onEvent(Throwable err) {
        Context ctx = Context.current();
        if(ctx == null){
            return;
        }

        LogUtils.error(ctx, err);
    }
}
