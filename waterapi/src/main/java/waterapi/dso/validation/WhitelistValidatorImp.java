package waterapi.dso.validation;

import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.solon.validation.annotation.WhitelistValidator;
import org.noear.water.WW;
import waterapi.dso.db.DbWaterCfgSafeApi;

public class WhitelistValidatorImp extends WhitelistValidator {

    @Override
    public Result validateOfContext(Context ctx, Whitelist anno, String name, StringBuilder tmp) {
        if (Solon.cfg().isWhiteMode()) {
            try {
                //先 token 验证
                String token = ctx.header(WW.water_acl_token);
                if (DbWaterCfgSafeApi.isWhitelistByToken(token)) {
                    return Result.succeed();
                }

                //再 ip 验证
                String ip = ctx.realIp();
                if (DbWaterCfgSafeApi.isWhitelistByIp(ip)) {
                    return Result.succeed();
                }

                return Result.failure(ip);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else {
            return Result.succeed();
        }
    }
}
