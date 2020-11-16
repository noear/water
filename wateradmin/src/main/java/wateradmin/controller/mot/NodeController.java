package wateradmin.controller.mot;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.models.water_reg.ServiceSpeedModel;

import java.sql.SQLException;
import java.util.List;

@Controller
@Mapping("/mot/")
public class NodeController extends BaseController {

    //性能监控
    @Mapping("node")
    public ModelAndView speed(String name,String sort) throws SQLException {
        List<ServiceSpeedModel> speeds = DbWaterOpsApi.getSpeedsByServiceAndName("_service",name, null, sort);
        viewModel.put("speeds",speeds);
        viewModel.put("serviceName","_service");
        return view("mot/node");
    }
}
