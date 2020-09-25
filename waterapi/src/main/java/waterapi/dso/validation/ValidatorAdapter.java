package waterapi.dso.validation;

import org.noear.snack.ONode;
import org.noear.solon.XApp;
import org.noear.solon.annotation.XBean;
import org.noear.solon.core.XPlugin;
import org.noear.solon.extend.validation.ValidatorManager;
import org.noear.solon.extend.validation.annotation.Whitelist;


@XBean
public class ValidatorAdapter implements XPlugin {
    @Override
    public void start(XApp app) {
        ValidatorManager.global().onFailure((ctx, ano, result, message) -> {
            ctx.setHandled(true);

            Class<?> type = ano.annotationType();

            if (type.equals(Whitelist.class)) {
                ONode data = new ONode();
                data.set("code", 2);
                data.set("msg", result.getDescription());//ip + ",not is whitelist!"

                ctx.outputAsJson(data.toJson());
            } else {
                ONode data = new ONode();
                data.set("code", 2);
                data.set("msg", "Parameter missing or error:" + result.getDescription());

                ctx.outputAsJson(data.toJson());
            }

            return true;
        });
    }
}
