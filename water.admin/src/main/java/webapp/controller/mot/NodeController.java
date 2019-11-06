package webapp.controller.mot;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.util.TextUtils;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import webapp.controller.BaseController;
import webapp.dao.BcfServiceChecker;
import webapp.dao.db.DbWindApi;
import webapp.models.water.ServiceSpeedModel;
import webapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author:Fei.chu
 * @Description:服务管理
 */

@XController
@XMapping("/mot/")
public class NodeController extends BaseController{

    //性能监控
    @XMapping("node")
    public ModelAndView speed(String name,String sort) throws SQLException {
        List<ServiceSpeedModel> speeds = DbWindApi.getSpeedsByServiceAndName("_service",name, null, sort);
        viewModel.put("speeds",speeds);
        viewModel.put("serviceName","_service");
        return view("mot/node");
    }
}
