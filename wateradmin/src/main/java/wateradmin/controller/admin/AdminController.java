package wateradmin.controller.admin;

import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.WW;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.SettingUtils;
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
@Mapping("admin")
@Controller
public class AdminController extends BaseController {

    @Mapping("")
    public ModelAndView home() throws SQLException {

        viewModel.put("subjectScale", SettingUtils.subjectScale().ordinal());
        return view("admin/home");
    }

    @Mapping("about")
    public ModelAndView about() throws SQLException {

        return view("admin/about");
    }

    @AuthPermissions(SessionPerms.admin)
    @Mapping("setting")
    public ModelAndView setting() throws SQLException {
        Map<String, String> sets = new LinkedHashMap<>();

        sets.put("alarm_sign", cfg("water", "alarm_sign").value);

        Properties props = cfg("water", WW.water_settings).getProp();
        props.forEach((k, v) -> {
            //water.setting.{*}
            sets.put((String) k, (String) v);
        });


        viewModel.put("name_topic","water.setting.scale.topic");
        viewModel.put("name_service","water.setting.scale.service");
        viewModel.put("name_subject","water.setting.scale.subject");

        viewModel.put("sets", sets);

        return view("admin/setting");
    }

    @AuthPermissions(SessionPerms.admin)
    @Mapping("setting/ajax/save")
    public Object setting_save(String json) throws Exception {
        if (TextUtils.isNotEmpty(json)) {
            ONode oNode = ONode.loadStr(json);

            //for alarm_sign
            DbWaterCfgApi.setConfigByTagName("water", "alarm_sign", oNode.get("alarm_sign").getString());

            //for water option
            StringBuilder options = new StringBuilder();
            oNode.forEach((k, v) -> {
                if (k.startsWith("water.setting.")) {
                    options.append(k).append("=").append(v.getString()).append("\n");
                    Solon.cfg().setProperty(k, v.getString());
                }
            });
            DbWaterCfgApi.setConfigByTagName("water", WW.water_settings, options.toString());

            //for bcf ldap
        }

        return new ViewModel().code(1);
    }


    private ConfigModel cfg(String tag, String name) throws SQLException {
        return DbWaterCfgApi.getConfigByTagName(tag, name);
    }
}
