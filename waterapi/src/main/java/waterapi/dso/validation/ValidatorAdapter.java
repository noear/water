package waterapi.dso.validation;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.validation.ValidatorManager;
import org.noear.solon.validation.annotation.Whitelist;
import waterapi.controller.UapiCodes;

@Component
public class ValidatorAdapter implements Plugin {
    @Override
    public void start(AppContext context) {
        ValidatorManager.register(Whitelist.class, new WhitelistValidatorImp());

        ValidatorManager.setFailureHandler((ctx, ano, result, message) -> {
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
