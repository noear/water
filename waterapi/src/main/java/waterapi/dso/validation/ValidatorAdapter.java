package waterapi.dso.validation;

import org.noear.solon.XApp;
import org.noear.solon.annotation.XBean;
import org.noear.solon.core.XPlugin;
import org.noear.solon.extend.validation.ValidatorManager;
import org.noear.solon.extend.validation.annotation.Whitelist;
import waterapi.controller.UapiCodes;

@XBean
public class ValidatorAdapter implements XPlugin {
    @Override
    public void start(XApp app) {
        ValidatorManager.global().register(Whitelist.class, new WhitelistValidatorImp());

        ValidatorManager.global().onFailure((ctx, ano, result, message) -> {
            ctx.setHandled(true);

            Class<?> type = ano.annotationType();

            if (type.equals(Whitelist.class)) {
                throw UapiCodes.CODE_16(result.getDescription());
            } else {
                throw UapiCodes.CODE_13(result.getDescription());
            }
        });
    }
}
