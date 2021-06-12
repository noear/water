package wateradmin.dso.valid;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.ValidatorFailureHandler;

import java.lang.annotation.Annotation;

/**
 * @author noear 2021/6/9 created
 */
@Component
public class ValidatorFailureHandlerImpl implements ValidatorFailureHandler {
    @Override
    public boolean onFailure(Context ctx, Annotation anno, Result rst, String message) {
        if (ctx.getHandled() || ctx.getRendered()) {
            return true;
        }

        ctx.setHandled(true);
        ctx.setRendered(true);

        if (Utils.isEmpty(message)) {
            if (Utils.isEmpty(rst.getDescription())) {
                message = new StringBuilder(100)
                        .append("@")
                        .append(anno.annotationType().getSimpleName())
                        .append(" verification failed")
                        .toString();
            } else {
                message = new StringBuilder(100)
                        .append("@")
                        .append(anno.annotationType().getSimpleName())
                        .append(" verification failed: ")
                        .append(rst.getDescription())
                        .toString();
            }
        }

        ctx.outputAsJson(new ONode().set("code", rst.getCode()).set("msg", message).toJson());

        return true;
    }
}
