package waterapp.controller.mot;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import waterapp.controller.BaseController;
import waterapp.dso.db.DbWaterOpsApi;
import waterapp.models.water_reg.ServiceSpeedModel;

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
        List<ServiceSpeedModel> speeds = DbWaterOpsApi.getSpeedsByServiceAndName("_service",name, null, sort);
        viewModel.put("speeds",speeds);
        viewModel.put("serviceName","_service");
        return view("mot/node");
    }
}
