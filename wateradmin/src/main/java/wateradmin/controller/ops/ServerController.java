package wateradmin.controller.ops;

import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.Session;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.models.water_ops.ServerModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@XController
@XMapping("/ops/")
public class ServerController extends BaseController {

    @XMapping("server")
    public ModelAndView project(String tag_name) throws SQLException {
        List<ServerModel> tags = DbWaterOpsApi.getServerTags();
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
        return view("ops/server");
    }

    @XMapping("server/inner")
    public ModelAndView projectInner(String tag_name, Integer _state) throws SQLException {
        if (_state != null) {
            viewModel.put("_state", _state);
            int state = _state;
            if (state == 0) {
                _state = 1;
            } else if (state == 1) {
                _state = 0;
            }
        }
        if (_state == null)
            _state = 1;

        List<ServerModel> list = DbWaterOpsApi.getServerByTagNameAndState(tag_name, _state);
        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);
        return view("ops/server_inner");
    }

    //禁用 启用服务
    @XMapping("server/disable")
    public ViewModel disable(Integer server_id, Integer is_enabled) throws SQLException {
        boolean is_admin = Session.current().getIsAdmin() > 0;
        if (is_admin == false) {
            return viewModel.code(0, "没有权限！");
        }
        boolean result = DbWaterOpsApi.disableServer(server_id, is_enabled);
        if (result) {
            viewModel.code(1, "操作成功！");
        } else {
            viewModel.code(0, "操作失败！");
        }

        return viewModel;
    }

    //删除 服务
    @XMapping("server/delete")
    public ViewModel delete(Integer server_id) throws SQLException {
        boolean is_admin = Session.current().getIsAdmin() > 0;
        if (is_admin == false) {
            return viewModel.code(0, "没有权限！");
        }

        boolean result = DbWaterOpsApi.deleteServer(server_id);
        if (result) {
            viewModel.code(1, "操作成功！");
        } else {
            viewModel.code(0, "操作失败！");
        }

        return viewModel;
    }


    //跳转服务编辑页面
    @XMapping("server/edit")
    public ModelAndView serverEdit(String tag_name, Integer server_id) throws SQLException {
        if(server_id == null){
            server_id = 0;
        }

        ServerModel server = DbWaterOpsApi.getServerByID(server_id);

        List<ServerModel> tags = DbWaterOpsApi.getServerTags();
        List<ConfigModel> accounts = DbWaterOpsApi.getIAASAccionts();

        viewModel.put("accounts", accounts);
        viewModel.put("tags", tags);
        viewModel.put("server", server);
        viewModel.put("tag_name", tag_name);

        return view("ops/server_edit");
    }

    //保存编辑
    @XMapping("server/edit/ajax/save")
    public ViewModel serverEditSave(Integer server_id, String tag, String name, String address, String address_local, Integer iaas_type, String iaas_key,  String iaas_account, String hosts_local, String note, Integer is_enabled, Integer env_type) throws SQLException {
        boolean is_admin = Session.current().getIsAdmin() > 0;

        if (is_admin == false) {
            return viewModel.code(0, "没有权限！");
        }

        boolean result = DbWaterOpsApi.updateServer(server_id, tag, name, address, address_local, iaas_type, iaas_key,  iaas_account, hosts_local, note, is_enabled, env_type);

        if (result) {
            viewModel.code(1, "保存成功！");
        } else {
            viewModel.code(0, "保存失败！");
        }

        return viewModel;
    }
}
