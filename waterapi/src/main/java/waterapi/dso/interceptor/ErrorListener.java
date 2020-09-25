package waterapi.dso.interceptor;


import org.noear.solon.annotation.XBean;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XEventListener;
import waterapi.dso.LogUtils;

@XBean
public class ErrorListener implements XEventListener<Throwable> {
    @Override
    public void onEvent(Throwable err) {
        XContext ctx = XContext.current();
        if(ctx == null){
            return;
        }

        LogUtils.error(ctx, err);
    }
}
