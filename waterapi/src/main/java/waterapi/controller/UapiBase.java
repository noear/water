package waterapi.controller;

import org.noear.snack.ONode;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Render;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.Valid;
import org.noear.water.WW;

@Valid
public class UapiBase implements Render {
    @Override
    public void render(Object obj, Context ctx) throws Throwable {
        if (obj == null) {
            return;
        }

        //统一添加版本号输出
        ctx.headerSet(WW.http_header_version, WW.water_version);

        if (obj instanceof String) {
            ctx.outputAsJson((String) obj);
        } else {
            if (obj instanceof ONode) {
                ctx.outputAsJson(((ONode) obj).toJson());
            } else {
                if (obj instanceof UapiCode) {
                    UapiCode err = (UapiCode) obj;
                    obj = Result.failure(err.getCode(), UapiCodes.getDescription(err));
                }

                if (obj instanceof Throwable) {
                    Throwable err = (Throwable) obj;
                    obj = Result.failure(err.getMessage());
                }

                ctx.outputAsJson(ONode.stringify(obj));
            }
        }
    }
}
