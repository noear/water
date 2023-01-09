package wateradmin.controller.sev;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.NotZero;
import wateradmin.controller.BaseController;
import wateradmin.dso.TagChecker;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterCfgUpstreamApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_cfg.GatewayModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 上游配置（以前叫网关配置）
 * */
@Controller
@Mapping("/sev/gateway")
public class UpstreamController extends BaseController {

    @Mapping("")
    public ModelAndView gateway(String tag_name) throws SQLException {
        List<TagCountsModel> tags = DbWaterCfgUpstreamApi.getGatewayTagList();

        //权限过滤
        TagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);

        return view("sev/gateway");
    }

    @Mapping("inner")
    public ModelAndView inner(String tag_name, int _state) throws SQLException {
        List<GatewayModel> list = DbWaterCfgUpstreamApi.getGatewayList(tag_name, _state == 0);

        viewModel.put("_state", _state);
        viewModel.put("list", list);

        return view("sev/gateway_inner");
    }

    @Mapping("edit")
    public ModelAndView edit(int gateway_id) throws SQLException {
        GatewayModel cfg = DbWaterCfgUpstreamApi.getGateway(gateway_id);
        if (cfg.gateway_id == 0) {
            cfg.is_enabled = 1;
        }

        viewModel.set("cfg", cfg);

        return view("sev/gateway_edit");

    }

    @AuthPermissions(SessionPerms.admin)
    @NotEmpty({"tag", "name"})
    @Mapping("ajax/save")
    public ViewModel save(int gateway_id, String tag, String name, String agent, String policy, int is_enabled) {

        try {
            gateway_id = DbWaterCfgUpstreamApi.saveGateway(gateway_id, tag, name, agent, policy, is_enabled);

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
        DbWaterCfgUpstreamApi.setGatewayEnabled(gateway_id, is_enabled);

        return viewModel.code(1, "");
    }

    @AuthPermissions(SessionPerms.admin)
    @NotZero("gateway_id")
    @Mapping("ajax/del")
    public ViewModel del(int gateway_id) throws SQLException {
        DbWaterCfgUpstreamApi.delGateway(gateway_id);

        return viewModel.code(1, "");
    }
}
