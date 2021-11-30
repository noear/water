package wateradmin.controller.cfg;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.NotZero;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterCfgGatewayApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.GatewayModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@Controller
@Mapping("/cfg/gateway")
public class GatewayController extends BaseController {

    @Mapping("")
    public ModelAndView gateway(String tag_name) throws SQLException {
        List<TagCountsModel> tags = DbWaterCfgGatewayApi.getGatewayTagList();

        //权限过滤
        BcfTagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);

        return view("cfg/gateway");
    }

    @Mapping("inner")
    public ModelAndView inner(String tag_name, Integer _state) throws SQLException {
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

        List<GatewayModel> list = DbWaterCfgGatewayApi.getGatewayList(tag_name, _state);

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

    @AuthPermissions(SessionPerms.admin)
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
    @AuthPermissions(SessionPerms.admin)
    @NotZero("gateway_id")
    @Mapping("ajax/enabled")
    public ViewModel enable(int gateway_id, int is_enabled) throws SQLException {
        DbWaterCfgGatewayApi.setGatewayEnabled(gateway_id, is_enabled);

        return viewModel.code(1, "");
    }

    @AuthPermissions(SessionPerms.admin)
    @NotZero("gateway_id")
    @Mapping("ajax/del")
    public ViewModel del(int gateway_id) throws SQLException {
        DbWaterCfgGatewayApi.delGateway(gateway_id);

        return viewModel.code(1, "");
    }
}
