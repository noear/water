package waterapi.dso.validation;

import org.noear.solon.core.XContext;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.solon.extend.validation.annotation.WhitelistValidator;
import waterapi.dso.IPUtils;
import waterapi.dso.db.DbWaterCfgApi;

import java.sql.SQLException;

public class WhitelistValidatorImp extends WhitelistValidator {
    @Override
    public XResult validate(XContext ctx, Whitelist anno, String name, StringBuilder tmp) {
        String ip = IPUtils.getIP(ctx);

        try {
            if (DbWaterCfgApi.isWhitelist(ip)) {
                return XResult.succeed();
            } else {
                return XResult.failure(ip);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
