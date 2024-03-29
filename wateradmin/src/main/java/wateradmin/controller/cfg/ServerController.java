package wateradmin.controller.cfg;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.models.water_ops.ServerModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@Controller
@Mapping("/cfg/")
public class ServerController extends BaseController {

    @Mapping("server")
    public ModelAndView home(String tag_name, int _state) throws SQLException {
        List<ServerModel> tags = DbWaterOpsApi.getServerTags();

        viewModel.put("_state", _state);
        viewModel.put("tags", tags);

        if (TextUtils.isEmpty(tag_name) == false) {
            viewModel.put("tag_name", tag_name);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag_name", tags.get(0).tag);
            } else {
                viewModel.put("tag_name", null);
            }
        }

        return view("cfg/server");
    }

    @Mapping("server/inner")
    public ModelAndView inner(String tag_name, int _state) throws SQLException {
        List<ServerModel> list = DbWaterOpsApi.getServerByTagNameAndState(tag_name, _state == 0);

        viewModel.put("_state", _state);
        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);

        return view("cfg/server_inner");
    }

    //禁用 启用服务
    @AuthPermissions(SessionPerms.admin)
    @Mapping("server/disable")
    public ViewModel disable(int server_id, int is_enabled) throws SQLException {
        boolean result = DbWaterOpsApi.disableServer(server_id, is_enabled);

        if (result) {
            viewModel.code(1, "操作成功！");
        } else {
            viewModel.code(0, "操作失败！");
        }

        return viewModel;
    }

    //删除 服务
    @AuthPermissions(SessionPerms.admin)
    @Mapping("server/delete")
    public ViewModel delete(int server_id) throws SQLException {
        boolean result = DbWaterOpsApi.deleteServer(server_id);

        if (result) {
            viewModel.code(1, "操作成功！");
        } else {
            viewModel.code(0, "操作失败！");
        }

        return viewModel;
    }


    //跳转服务编辑页面
    @Mapping("server/edit")
    public ModelAndView edit(String tag_name, int server_id) throws SQLException {
        ServerModel server = DbWaterOpsApi.getServerByID(server_id);

        if (server.server_id == 0) {
            //默认为启用
            server.is_enabled = 1;
        }


        List<ServerModel> tags = DbWaterOpsApi.getServerTags();
        List<ConfigModel> accounts = DbWaterOpsApi.getIAASAccionts();

        viewModel.put("accounts", accounts);
        viewModel.put("tags", tags);
        viewModel.put("server", server);
        viewModel.put("tag_name", tag_name);

        return view("cfg/server_edit");
    }

    //保存编辑
    @AuthPermissions(SessionPerms.admin)
    @Mapping("server/edit/ajax/save")
    public ViewModel saveDo(int server_id, String tag, String name, String address, String address_local, int iaas_type, String iaas_key, String iaas_account, String hosts_local, String note, int is_enabled, int env_type) throws SQLException {
        boolean result = DbWaterOpsApi.updateServer(server_id, tag, name, address, address_local, iaas_type, iaas_key, iaas_account, hosts_local, note, is_enabled, env_type);

        if (result) {
            viewModel.code(1, "保存成功！");
        } else {
            viewModel.code(0, "保存失败！");
        }

        return viewModel;
    }
}
