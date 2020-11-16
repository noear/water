package waterapi.dso.validation;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.Plugin;
import org.noear.solon.extend.validation.ValidatorManager;
import org.noear.solon.extend.validation.annotation.Whitelist;
import waterapi.controller.UapiCodes;

@Component
public class ValidatorAdapter implements Plugin {
    @Override
    public void start(SolonApp app) {
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
