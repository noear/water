package wateradmin.controller.mot;

import com.alibaba.fastjson.JSONObject;
import org.noear.snack.ONode;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfServiceChecker;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.models.water_reg.ServiceSpeedModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
@Mapping("/mot/")
public class SpeedController extends BaseController {


    //性能监控
    @Mapping("speed")
    public ModelAndView speed(String serviceName) throws SQLException {
        List<ServiceSpeedModel> services = DbWaterOpsApi.getSpeedServices();

        BcfServiceChecker.filter(services, m -> m.service);

        if (TextUtils.isEmpty(serviceName) == false) {
            viewModel.put("serviceName", serviceName);
        } else {
            if (services.isEmpty() == false && services.size()>0) {
                viewModel.put("serviceName", services.get(0).service);
            } else {
                viewModel.put("serviceName", null);
            }
        }
        viewModel.put("services", services);
        return view("mot/speed");
    }

    //性能监控-列表
    @Mapping("speed/inner")
    public ModelAndView speedList(String serviceName,String name, String sort) throws SQLException {
        List<ServiceSpeedModel> speeds = DbWaterOpsApi.getSpeedsByServiceAndName(serviceName,null,name, sort);
        viewModel.put("speeds",speeds);
        viewModel.put("serviceName",serviceName);
        return view("mot/speed_inner");
    }

    //性能监控图标统计
    @Mapping("speed/charts")
    public ModelAndView speedCharts(String tag,String name_md5,String service) throws SQLException{
        Map<String,Object> speedReqTate = DbWaterOpsApi.getSpeedForDate(tag, name_md5, service,"total_num");
        Map<String,Object> speeds = DbWaterOpsApi.getSpeedForMonth(tag, name_md5, service);
        viewModel.put("speedReqTate", ONode.stringify(speedReqTate));
        viewModel.put("speeds", ONode.stringify(speeds));
        viewModel.put("tag",tag);
        viewModel.put("name", WaterClient.Track.getName(name_md5));
        viewModel.put("name_md5",name_md5);
        viewModel.put("service",service);
        return view("mot/speed_charts");
    }
    @Mapping("speed/charts/ajax/reqtate")
    public ViewModel speedCharts_reqtate(String tag,String name,String service, Integer type) throws SQLException{
        String valField = "total_num";
        if(type == null){type=0;}
        switch (type){
            case 0:valField ="total_num"; break;
            case 1:valField ="total_num_slow1"; break;
            case 2:valField ="total_num_slow2"; break;
            case 3:valField ="total_num_slow5"; break;
            case 4:valField ="average"; break;
            case 5:valField ="fastest"; break;
            case 6:valField ="slowest"; break;
        }

        Map<String,Object> speedReqTate = DbWaterOpsApi.getSpeedForDate(tag, name, service, valField);
        viewModel.put("speedReqTate",speedReqTate);
        viewModel.put("tag",tag);
        viewModel.put("name",name);
        viewModel.put("service",service);
        return viewModel;
    }

}
