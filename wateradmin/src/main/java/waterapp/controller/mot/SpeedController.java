package waterapp.controller.mot;

import com.alibaba.fastjson.JSONObject;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import waterapp.controller.BaseController;
import waterapp.dso.BcfServiceChecker;
import waterapp.dso.db.DbWaterOpsApi;
import waterapp.viewModels.ViewModel;
import waterapp.models.water_reg.ServiceSpeedModel;

import java.sql.SQLException;
import java.util.List;

@XController
@XMapping("/mot/")
public class SpeedController extends BaseController{


    //性能监控
    @XMapping("speed")
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
    @XMapping("speed/inner")
    public ModelAndView speedList(String serviceName,String name, String sort) throws SQLException {
        List<ServiceSpeedModel> speeds = DbWaterOpsApi.getSpeedsByServiceAndName(serviceName,null,name, sort);
        viewModel.put("speeds",speeds);
        viewModel.put("serviceName",serviceName);
        return view("mot/speed_inner");
    }

    //性能监控图标统计
    @XMapping("speed/charts")
    public ModelAndView speedCharts(String tag,String name,String service) throws SQLException{
        JSONObject speedReqTate = DbWaterOpsApi.getSpeedForDate(tag, name, service,"total_num");
        JSONObject speeds = DbWaterOpsApi.getSpeedForMonth(tag, name, service);
        viewModel.put("speedReqTate",speedReqTate.toJSONString());
        viewModel.put("speeds",speeds.toJSONString());
        viewModel.put("tag",tag);
        viewModel.put("name",name);
        viewModel.put("service",service);
        return view("mot/speed_charts");
    }
    @XMapping("speed/charts/ajax/reqtate")
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

        JSONObject speedReqTate = DbWaterOpsApi.getSpeedForDate(tag, name, service, valField);
        viewModel.put("speedReqTate",speedReqTate);
        viewModel.put("tag",tag);
        viewModel.put("name",name);
        viewModel.put("service",service);
        return viewModel;
    }

}
