package webapp.controller.mot;

import org.noear.snack.ONode;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.admin.tools.dso.Session;
import webapp.dao.db.DbWaterServiceApi;
import webapp.models.water.ServiceModel;

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
        List<ServiceModel> services = DbWaterServiceApi.getServices(name , is_web ,_state);

        viewModel.put("is_web", is_web);
        viewModel.put("services", services);
        viewModel.put("name",name);
        return view("mot/service");
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
        List<ServiceModel> services = DbWaterServiceApi.getServices(name,"web".equals(_type),_state);
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

        boolean result = DbWaterServiceApi.delServiceById(service_id);
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

        boolean result = DbWaterServiceApi.disService(service_id,is_enabled);
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
            ServiceModel m = DbWaterServiceApi.getServiceById(service_id);
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
            boolean result = DbWaterServiceApi.udpService(service_id,name,address,note,check_type,check_url);
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
