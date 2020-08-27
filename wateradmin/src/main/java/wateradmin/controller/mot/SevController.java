package wateradmin.controller.mot;

import org.noear.snack.ONode;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.solon.core.XContext;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.Session;
import wateradmin.dso.db.DbWaterRegApi;
import wateradmin.models.water_reg.ServiceModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;


@XController
@XMapping("/mot/")
public class SevController extends BaseController {

    //服务状态
    @XMapping("/service")
    public ModelAndView index(String name,Integer _state, String _type) throws SQLException {

        if (_state != null) {
            viewModel.put("_state", _state);
            int state = _state;
            if (state == 0) {
                _state = 1;
            } else if (state == 1) {
                _state = 0;
            }
        }
        boolean is_web = "web".equals(_type);
        if (_state == null)
            _state = 1;
        List<ServiceModel> services = DbWaterRegApi.getServices(name , is_web ,_state);

        viewModel.put("is_web", is_web);
        viewModel.put("services", services);
        viewModel.put("name",name);
        return view("mot/service");
    }

    @XMapping("/service/check")
    public String service_check(String s) throws Exception{
        if(TextUtils.isEmpty(s)){
            return null;
        }

        if(s.indexOf("@") < 0 || s.indexOf(":") < 0){
            return null;
        }

        String ca = s.split("@")[1];

        String url = "http://" + ca + "/run/status/";

        return HttpUtils.getString(url);
    }

    //页面自动刷新获取表单数据
    @XMapping("/service/ajax/service_table")
    public ModelAndView manageS_table(String name,Integer _state, String _type) throws SQLException {
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
        List<ServiceModel> services = DbWaterRegApi.getServices(name,"web".equals(_type),_state);
        viewModel.put("services", services);
        return view("mot/service_table");
    }

    //删除服务
    @XMapping("/service/ajax/deleteService")
    public ViewModel deleteServiceById(Integer service_id) throws SQLException {
        boolean is_admin = Session.current().getIsAdmin()>0;
        if (is_admin == false) {
            return viewModel.code(0,"没有权限！");
        }

        boolean result = DbWaterRegApi.deleteServiceById(service_id);
        if (result){
            viewModel.code(1,"删除成功！");
        }else{
            viewModel.code(0,"删除失败！");
        }

        return viewModel;
    }

    //启用 | 禁用 服务
    @XMapping("/service/ajax/disable")
    public ViewModel disable(Integer service_id,Integer is_enabled) throws SQLException {
        boolean is_admin = Session.current().getIsAdmin()>0;
        if (is_admin == false) {
            return viewModel.code(0,"没有权限！");
        }

        boolean result = DbWaterRegApi.disableService(service_id,is_enabled);
        if (result){
            viewModel.code(1,"操作成功！");
        }else{
            viewModel.code(0,"操作失败！");
        }

        return viewModel;
    }

    //服务状态
    @XMapping("/service/edit")
    public ModelAndView service_edit(Integer service_id) throws SQLException {
        if(service_id!=null) {
            ServiceModel m = DbWaterRegApi.getServiceById(service_id);
            viewModel.put("data", ONode.load(m).toJson());
        }else{
            viewModel.put("data", ONode.load(new ServiceModel()).toJson());
        }

        viewModel.put("service_id",service_id);

        return view("mot/service_edit");
    }
    @XMapping("/service/edit/ajax/save")
    public ViewModel service_edit_ajax_save(Integer service_id,String name,String address,String note,Integer check_type,String check_url) throws SQLException {
        int is_admin = Session.current().getIsAdmin();

        if(is_admin==1) {
            boolean result = DbWaterRegApi.udpService(service_id,name,address,note,check_type,check_url);
            if (result) {
                viewModel.code(1,"保存成功！");
            } else {
                viewModel.code(0,"保存失败！");
            }
        }else{
            viewModel.code(0,"没有权限！");
        }

        return viewModel;
    }
}
