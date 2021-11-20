package wateradmin.controller.admin;

import org.noear.bcf.BcfClient;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthRoles;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.Session;
import wateradmin.dso.SessionRoles;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author noear 2021/11/20 created
 */
@Controller
public class AdminController extends BaseController {

    @AuthRoles(SessionRoles.role_admin)
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

    @AuthRoles(SessionRoles.role_admin)
    @Mapping("/admin/ajax/save")
    public Object home_save(String json) throws Exception {
        if (TextUtils.isNotEmpty(json)) {
            ONode oNode = ONode.loadStr(json);

            //for water option
            StringBuilder options = new StringBuilder();
            oNode.forEach((k, v) -> {
                if (k.startsWith("water.option.")) {
                    options.append(k).append("=").append(v.getString()).append("\n");
                }
            });
            DbWaterCfgApi.setConfigByTagName("water", "wateradmin.yml", options.toString());

            //for user.newpwd
            if (oNode.contains("user.newpwd")) {
                String oldpwd = oNode.get("user.oldpwd").getString();
                String newpwd = oNode.get("user.newpwd").getString();
                String newpwd2 = oNode.get("user.newpwd2").getString();

                if (newpwd.length() > 0 && newpwd.equals(newpwd2)) {
                    BcfClient.setUserPassword(Session.current().getUserId(), oldpwd, newpwd);
                }
            }
        }

        return new ViewModel().code(1);
    }


    private ConfigModel cfg(String tag, String name) throws SQLException {
        return DbWaterCfgApi.getConfigByTagName(tag, name);
    }
}
