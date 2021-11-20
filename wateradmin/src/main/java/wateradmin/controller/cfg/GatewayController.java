package wateradmin.controller.cfg;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthRoles;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.NotZero;
import org.noear.water.model.TagCountsM;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.SessionRoles;
import wateradmin.dso.SetsUtils;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.db.DbWaterCfgGatewayApi;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.dso.db.DbWaterRegApi;
import wateradmin.models.water_cfg.GatewayModel;
import wateradmin.models.water_reg.GatewayVoModel;
import wateradmin.models.water_reg.ServiceConsumerModel;
import wateradmin.models.water_reg.ServiceModel;
import wateradmin.models.water_reg.ServiceSpeedModel;
import wateradmin.viewModels.ViewModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Mapping("/cfg/gateway")
public class GatewayController extends BaseController {

    private static final String SEV_SERVER_TAG = "_service";

    @Mapping("")
    public ModelAndView gateway(Integer _state) throws SQLException {
        if (_state != null) {
            viewModel.put("_state", _state);
            int state = _state;
            if (state == 0) {
                _state = 1;
            } else if (state == 1) {
                _state = 0;
            }
        }

        if (_state == null) {
            _state = 1;
        }

        if (SetsUtils.waterSettingScale() > 1) {
            //中等以上模规
            List<TagCountsM> tags = DbWaterCfgGatewayApi.getGatewayTagList();

            viewModel.put("tags", tags);

            return view("cfg/gateway");
        } else {
            //小或中
            List<GatewayModel> list = DbWaterCfgGatewayApi.getGatewayList(null, _state);

            viewModel.put("list", list);

            return view("cfg/gateway_inner");
        }
    }

    @Mapping("inner")
    public ModelAndView inner(String tag, Integer _state) throws SQLException {
        if (_state != null) {
            viewModel.put("_state", _state);
            int state = _state;
            if (state == 0) {
                _state = 1;
            } else if (state == 1) {
                _state = 0;
            }
        }

        if (_state == null) {
            _state = 1;
        }

        List<GatewayModel> list = DbWaterCfgGatewayApi.getGatewayList(tag, _state);

        viewModel.put("list", list);

        return view("cfg/gateway_inner");
    }

    @Mapping("edit")
    public ModelAndView edit(int gateway_id) throws SQLException {
        GatewayModel cfg = DbWaterCfgGatewayApi.getGateway(gateway_id);
        if (cfg.gateway_id == 0) {
            cfg.is_enabled = 1;
        }

        viewModel.set("cfg", cfg);

        return view("cfg/gateway_edit");

    }

    @AuthRoles(SessionRoles.role_admin)
    @NotEmpty({"tag", "name"})
    @Mapping("ajax/save")
    public ViewModel save(int gateway_id, String tag, String name, String agent, String policy, int is_enabled) {

        try {
            gateway_id = DbWaterCfgGatewayApi.saveGateway(gateway_id, tag, name, agent, policy, is_enabled);

            viewModel.put("gateway_id", gateway_id);
            viewModel.code(1, "成功");
        } catch (Exception e) {
            viewModel.code(0, Utils.throwableToString(e));
        }

        return viewModel;

    }

    //日志启用/禁用
    @AuthRoles(SessionRoles.role_admin)
    @NotZero("gateway_id")
    @Mapping("ajax/enabled")
    public ViewModel brokerEnable(int gateway_id, int is_enabled) throws SQLException {
        DbWaterCfgGatewayApi.setGatewayEnabled(gateway_id, is_enabled);

        return viewModel.code(1, "保存成功！");
    }
}
