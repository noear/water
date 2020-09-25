package waterapi.controller;

import org.noear.snack.ONode;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XRender;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.XValid;

@XValid
public class UapiBase implements XRender {
    @Override
    public void render(Object obj, XContext ctx) throws Throwable {
        if (obj == null) {
            return;
        }

        if (obj instanceof String) {
            ctx.outputAsJson((String) obj);
        } else {
            if (obj instanceof ONode) {
                ctx.outputAsJson(((ONode) obj).toJson());
            } else {
                if (obj instanceof UapiCode) {
                    UapiCode err = (UapiCode) obj;
                    obj = XResult.failure(err.getCode(), UapiCodes.getDescription(err));
                }

                if (obj instanceof Throwable) {
                    Throwable err = (Throwable) obj;
                    obj = XResult.failure(err.getMessage());
                }

                ctx.outputAsJson(ONode.stringify(obj));
            }
        }
    }
}
