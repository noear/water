package wateradmin.controller.mot;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.models.water_reg.ServiceSpeedModel;

import java.sql.SQLException;
import java.util.List;

@XController
@XMapping("/mot/")
public class NodeController extends BaseController {

    //性能监控
    @XMapping("node")
    public ModelAndView speed(String name,String sort) throws SQLException {
        List<ServiceSpeedModel> speeds = DbWaterOpsApi.getSpeedsByServiceAndName("_service",name, null, sort);
        viewModel.put("speeds",speeds);
        viewModel.put("serviceName","_service");
        return view("mot/node");
    }
}
