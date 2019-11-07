package webapp.controller.ops;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.dso.Session;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;
import webapp.dao.db.DbWaterApi;
import webapp.dao.db.DbWindApi;
import webapp.dao.ops.ShellResult;
import webapp.dao.ops.ShellText;
import webapp.dao.ops.ShellUtil;
import webapp.models.water.*;
import webapp.models.water_wind.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@XController
@XMapping("/ops/")
public class ServerController extends BaseController {

    @XMapping("server")
    public ModelAndView project(String tag) throws SQLException {
        List<WindServerModel> tags = DbWindApi.getServerTags();
        viewModel.put("tags", tags);
        if (TextUtils.isEmpty(tag) == false) {
            viewModel.put("tag", tag);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag", tags.get(0).tag);
            } else {
                viewModel.put("tag", null);
            }
        }
        return view("ops/server");
    }

    @XMapping("server/inner")
    public ModelAndView projectInner(String tag, Integer _state) throws SQLException {
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

        List<WindServerModel> list = DbWindApi.getServerByTagNameAndState(tag, _state);
        viewModel.put("list", list);
        viewModel.put("tag", tag);
        return view("ops/server_inner");
    }

    //禁用 启用服务
    @XMapping("server/disable")
    public ViewModel disable(Integer server_id, Integer is_enabled) throws SQLException {
        boolean is_admin = Session.current().getIsAdmin() > 0;
        if (is_admin == false) {
            return viewModel.code(0, "没有权限！");
        }
        boolean result = DbWindApi.disableServer(server_id, is_enabled);
        if (result) {
            viewModel.code(1, "操作成功！");
        } else {
            viewModel.code(0, "操作失败！");
        }

        return viewModel;
    }

    //跳转服务新增页面
    @XMapping("server/add")
    public ModelAndView serverAdd(String tag) throws SQLException {
        List<WindServerModel> tags = DbWindApi.getServerTags();
        List<ConfigModel> accounts = DbWindApi.getIAASAccionts();

        viewModel.put("accounts", accounts);
        viewModel.put("tags", tags);
        viewModel.put("tag", tag);
        viewModel.put("server", new WindServerModel());
        return view("ops/server_edit");
    }

    //跳转服务编辑页面
    @XMapping("server/edit")
    public ModelAndView serverEdit(Integer server_id) throws SQLException {
        WindServerModel server = DbWindApi.getServerByID(server_id);
        List<WindServerModel> tags = DbWindApi.getServerTags();

        List<ConfigModel> accounts = DbWindApi.getIAASAccionts();

        viewModel.put("accounts", accounts);
        viewModel.put("tags", tags);
        viewModel.put("server", server);
        return view("ops/server_edit");
    }

    //保存编辑
    @XMapping("server/edit/ajax/save")
    public ViewModel serverEditSave(Integer server_id, String tag, String name, String address, String address_local, Integer iaas_type, String iaas_key, String iaas_account, String hosts_local, String note, Integer is_enabled, Integer env_type) throws SQLException {
        boolean is_admin = Session.current().getIsAdmin() > 0;

        if (is_admin == false) {
            return viewModel.code(0, "没有权限！");
        }

        boolean result = DbWindApi.updateServer(server_id, tag, name, address, address_local, iaas_type, iaas_key, iaas_account, hosts_local, note, is_enabled, env_type);

        if (result) {
            viewModel.code(1, "保存成功！");
        } else {
            viewModel.code(0, "保存失败！");
        }

        return viewModel;
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 计算资源附加配置
     * @Date:11:00 2018/12/11
     */
    @XMapping("server/extra")
    public ModelAndView serverExtra(Integer server_id) throws Exception {
        WindServerModel server = DbWindApi.getServerByID(server_id);
        List<ConfigModel> accounts = DbWindApi.getIAASAccionts();

        ConfigModel cfg = DbWindApi.getServerIaasAccount(server.iaas_key);

        if (cfg != null) {
//            DescribeInstancesResponse.Instance instance = AliyunCmsUtil.getInstanceInfo(cfg, server.iaas_key);
//            int size = AliyunCmsUtil.getEcsDiskInfo(cfg, server.iaas_key).getSize();
//            viewModel.set("instance", instance);
//            viewModel.set("size", size);
        }
        List<WindOperateModel> operateList = DbWindApi.getOperateByServerId(server_id);
        viewModel.put("operateList", operateList);
        viewModel.put("accounts", accounts);
        viewModel.put("server", server);

        if (!TextUtils.isEmpty(server.address_local)) {
            List<ServiceModel> services = DbWaterApi.getServices("ip:" + server.address_local,false, 1);

            List<String> names = services.stream().map(m->m.name).collect(Collectors.toList());
            Map<String, String> map = DbWindApi.getServiceDomains(names);
            viewModel.put("services", services);
            viewModel.put("domains", map);
        }

        return view("ops/server_extra");
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 命令配置
     * @Date:13:44 2018/12/11
     */
    @XMapping("server/extra/operate")
    public ModelAndView operate(Integer server_id) throws Exception {
        WindServerModel server = DbWindApi.getServerByID(server_id);
        List<WindOperateModel> operateList = DbWindApi.getOperateByServerId(server_id);
        List<WindScriptModel> scriptTags = DbWindApi.getScriptTags();

        List<ConfigModel> accounts = DbWindApi.getIAASAccionts();

        ConfigModel cfg = DbWindApi.getServerIaasAccount(server.iaas_key);

        if (cfg != null) {
//            DescribeInstancesResponse.Instance instance = AliyunCmsUtil.getInstanceInfo(cfg, server.iaas_key);
//            int size = AliyunCmsUtil.getEcsDiskInfo(cfg, server.iaas_key).getSize();
//            viewModel.set("instance", instance);
//            viewModel.set("size", size);
        }
        viewModel.put("server_id", server_id);
        viewModel.put("accounts", accounts);
        viewModel.put("operateList", operateList);
        viewModel.put("scriptTags", scriptTags);
        return view("ops/server_extra_operate");
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 根据tag获取对应脚本
     * @Date:14:46 2018/12/11
     */
    @XMapping("server/extra/operate/ajax/getScript")
    public ViewModel getScript(String tag) throws SQLException {
        List<WindScriptModel> scripts = DbWindApi.getScriptByTagNameAndState(tag, 1);
        viewModel.set("scripts", scripts);
        return viewModel;
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 保存操作配置
     * @Date:13:14 2018/12/12
     */
    @XMapping("server/extra/operate/ajax/save")
    public ViewModel saveOperate(Integer server_id, Long operate_id,String [] args, String name, Integer script_id, Integer rank) throws Exception {
        Long result = DbWindApi.setOperate(operate_id, name, 0, server_id, script_id, rank);
        //清除之前保存的数值
        DbWindApi.deleteActualParam(operate_id);
        if (result>0) {
            if (operate_id==null || operate_id == 0L){
                operate_id = result;
            }
            for (int i = 0; i < args.length; i++) {
                Boolean paramResult = DbWindApi.setActualParam(operate_id,script_id,i+1,args[i]);
                if (!paramResult) {
                    viewModel.code(0,"参数保存异常！");
                    return viewModel;
                }
            }
            viewModel.code(1, "保存成功！");
        } else {
            viewModel.code(0, "保存失败！");
        }
        return viewModel;
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 删除操作配置
     * @Date:16:17 2018/12/12
     */
    @XMapping("server/extra/operate/ajax/delete")
    public ViewModel deleteOperate(Long operate_id) throws SQLException {
        if (operate_id==null || operate_id == 0L){
            viewModel.code(0,"请求异常,请重试！");
            return viewModel;
        }
        if (Session.current().getIsAdmin() == 0) {
            viewModel.code(0, "没有删除权限！");
        }
        Boolean result = DbWindApi.deleteOperate(operate_id);
        if (result) {
            DbWindApi.deleteActualParam(operate_id);
            viewModel.code(1, "删除成功！");
        } else {
            viewModel.code(0, "删除失败！");
        }
        return viewModel;
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 执行操作
     * @Date:18:01 2018/12/12
     */
    @XMapping("server/extra/operate/ajax/exec")
    public ViewModel execOperate(Integer operate_id) throws SQLException {
        WindOperateModel operate = DbWindApi.getOperateById(operate_id);
        WindServerModel server = DbWindApi.getServerByID(operate.server_id);
        WindScriptModel script = DbWindApi.getScriptByID(operate.script_id);

        String args = DbWindApi.getOperateParam(operate.operate_id, operate.script_id);

        ShellResult ret = ShellUtil.exec(server.address, args, script.code);

        if (ret.isOk) {
            String output = ret.output;
            viewModel.code(1, output);
        } else {
            viewModel.code(0, "执行失败！");
        }
        return viewModel;
    }

    //重启服务
    @XMapping("/server/extra/service/ajax/restart")
    public ViewModel restart(String ip, String domain, Integer service_id) throws SQLException {
        boolean is_admin = Session.current().getIsAdmin()>0;
        if (is_admin == false) {
            return viewModel.code(0,"没有权限！");
        }

        ServiceModel service = DbWaterApi.getServiceById(service_id);
        StringBuilder sb = new StringBuilder();
        sb.append("port=").append(service.port).append(";")
          .append("project=").append(service.name).append(";")
          .append("domain=").append(domain).append(";");
        boolean result = ShellUtil.exec(ip, sb.toString(), ShellText.RESTART_SERVICE).isOk;

        if (result){
            viewModel.code(1,"操作成功！");
        }else{
            viewModel.code(0,"操作失败！");
        }

        return viewModel;
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 编辑操作
     * @Date:13:13 2018/12/18
     */
    @XMapping("/server/extra/operate/edit")
    public ModelAndView editOperate(Integer server_id,Integer operate_id) throws SQLException {
        WindOperateModel operate = new WindOperateModel();
        if (operate_id != null && operate_id > 0) {
            operate = DbWindApi.getOperateById(operate_id);
            WindScriptModel script = DbWindApi.getScriptByID(operate.script_id);
            operate.script_tag = script.tag;
            operate.script_name = script.name;
        }
        List<WindScriptModel> scriptTags = DbWindApi.getScriptTags();
        viewModel.put("server_id",server_id);
        viewModel.put("scriptTags", scriptTags);
        viewModel.put("operate", operate);
        return view("/ops/server_extra_operate_edit");
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 获取对应脚本参数值
     * @Date:13:13 2018/12/18
     */
    @XMapping("server/extra/operate/ajax/getArgs")
    public ViewModel getArgs(Integer operate_id, Integer script_id) throws SQLException {

        List<WindFormalParamModel> argList = DbWindApi.getFormalParam(script_id);
        argList.forEach(arg->{arg.param_value = "";});
        if (operate_id != null && operate_id > 0) {
            List<WindActualParamModel> apList = DbWindApi.getActualParam(operate_id, script_id);
            if (apList.size() > 0) {
                HashMap<Integer, String> apMap = new HashMap<>();
                apList.forEach(ap -> {
                    apMap.put(ap.param_id, ap.param_value);
                });
                argList.forEach(arg -> {
                    arg.param_value = apMap.getOrDefault(arg.param_id,"");
                });
            }
        }
        viewModel.set("argList", argList);
        return viewModel;
    }
}
