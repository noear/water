package wateradmin.controller.admin;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.WaterClient;
import wateradmin.controller.BaseController;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.water_cfg.ConfigModel;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author noear 2021/11/20 created
 */
@Controller
public class AdminController extends BaseController {
    @Mapping("/admin")
    public ModelAndView home() throws SQLException {
        Map<String, String> sets = new LinkedHashMap<>();

        sets.put("alarm_sign", cfg("water", "alarm_sign").value);
        Properties props = cfg("water", "wateradmin.yml").getProp();
        props.forEach((k, v) -> {
            //water.setting.{*}
            sets.put((String) k, (String) v);
        });


        viewModel.put("sets", sets);

        return view("admin/setting");
    }

    private ConfigModel cfg(String tag, String name) throws SQLException {
        return DbWaterCfgApi.getConfigByTagName(tag, name);
    }
}
