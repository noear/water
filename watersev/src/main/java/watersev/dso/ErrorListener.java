package watersev.dso;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.event.EventListener;
import org.noear.solon.logging.utils.TagsMDC;

/**
 * 异常监听
 *
 * @author noear 2021/10/31 created
 */
@Slf4j
@Component
public class ErrorListener implements EventListener<Throwable> {
    @Override
    public void onEvent(Throwable err) {
        TagsMDC.tag0("global");
        log.error("{}", err);
    }
}
