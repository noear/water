package waterapi.controller.config;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import waterapi.models.I18nModel;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterCfgApi;
import waterapi.dso.interceptor.Logging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取语言包
 *
 * @author noear
 * @since 2022.04
 */
@Logging
@Whitelist
@Controller
public class CMD_i18n_get extends UapiBase {
    @NotEmpty({"tag", "bundle"})
    @Mapping("/i18n/get/")
    public Result cmd_exec(String tag, String bundle, String lang) throws Throwable {
        List<I18nModel> list = DbWaterCfgApi.getI18nListByTag(tag, bundle, lang);
        Map<String, String> map = new HashMap<>();
        list.forEach((m) -> map.put(m.name, m.value));

        return Result.succeed(map);
    }
}
