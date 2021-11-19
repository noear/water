package waterraas.dso;

import lombok.extern.slf4j.Slf4j;
import org.noear.snack.ONode;
import org.noear.solon.core.event.EventListener;
import org.noear.solon.core.handle.Context;
import org.slf4j.MDC;

/**
 * @author noear 2021/10/28 created
 */
@Slf4j
public class ErrorListener implements EventListener<Throwable> {

    @Override
    public void onEvent(Throwable ex) {
        Context ctx = Context.current();

        if (ctx == null) {
            MDC.put("tag0", "global");
            log.error("{}", ex);
        } else {
            MDC.put("tag0", ctx.path());

            String param = ONode.stringify(ctx.paramMap());
            log.error("> Param: {}\n\n< Error: {}", param, ex);
        }
    }
}
