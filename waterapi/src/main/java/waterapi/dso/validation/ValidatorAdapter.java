package waterapi.dso.validation;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.validation.ValidatorManager;
import org.noear.solon.validation.annotation.Whitelist;
import waterapi.controller.UapiCodes;

@Configuration
public class ValidatorAdapter {
    @Bean
    public void initWhitelist() {
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
