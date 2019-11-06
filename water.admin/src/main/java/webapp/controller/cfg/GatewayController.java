package webapp.controller.cfg;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.tools.TextUtils;
import org.noear.water.tools.ThrowableUtils;
import webapp.controller.BaseController;
import webapp.dao.db.DbWaterApi;
import webapp.dao.db.DbWindApi;
import webapp.models.vo.GatewayVoModel;
import webapp.models.water.ConfigModel;
import webapp.models.water.ServiceConsumerModel;
import webapp.models.water.ServiceModel;
import webapp.models.water.ServiceSpeedModel;
import webapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@XController
@XMapping("/cfg/gateway")
public class GatewayController extends BaseController {

    private static final String SEV_CONFIG_TAG = "_service";

    @XMapping("")
    public ModelAndView gateway() throws SQLException {

        List<ConfigModel> sevs = DbWaterApi.getGateways();

        viewModel.set("sevs", sevs);

        viewModel.set("sev_key", sevs.size() > 0 ? sevs.get(0).key : null);

        return view("cfg/gateway");

    }

    @XMapping("inner")
    public ModelAndView inner(String sev_key) throws SQLException {

        ConfigModel cfg = DbWaterApi.getConfigByTagName(SEV_CONFIG_TAG, sev_key);
        if(TextUtils.isEmpty(cfg.user)==false){ //通过 cfg.user, 实现别名与实名的情况
            sev_key = cfg.user;
        }

        double pdsTotal = 0.01;

        List<ServiceModel> sevs = DbWaterApi.getServicesByName(sev_key);

        List<ServiceSpeedModel> sevPds = DbWindApi.getServiceSpeedByService(SEV_CONFIG_TAG);

        List<ServiceConsumerModel> csms = DbWaterApi.getServiceConsumers(sev_key);

        List<ServiceSpeedModel> csmPds = DbWindApi.getServiceSpeedByService("_from",sev_key);

        List<GatewayVoModel> gtws = new ArrayList<>();

        for (ServiceModel sev : sevs) {
            GatewayVoModel gtw = new GatewayVoModel();
            gtw.service = sev;

            for (ServiceSpeedModel spd : sevPds) {
                if (TextUtils.equals(sev.address, spd.name)) {
                    gtw.speed = spd;
                    pdsTotal += spd.total_num;
                    break;
                }
            }

            gtws.add(gtw);
        }

        for(ServiceConsumerModel m : csms){
            for (ServiceSpeedModel spd : csmPds) {
                if (TextUtils.equals(m.consumer+"@"+m.consumer_address, spd.name)) {
                    m.traffic_num = spd.total_num;
                    m.traffic_per = (spd.total_num/pdsTotal)*100;
                    break;
                }
            }
        }

        viewModel.set("sev_key", sev_key);
        viewModel.set("cfg", cfg);
        viewModel.set("gtws", gtws);
        viewModel.set("csms", csms);

        return view("cfg/gateway_inner");

    }

    @XMapping("add")
    public ModelAndView add() {

        return view("cfg/gateway_edit");

    }

    @XMapping("edit/{sev_key}")
    public ModelAndView edit(String sev_key) throws SQLException {

        ConfigModel cfg = DbWaterApi.getConfigByTagName(SEV_CONFIG_TAG, sev_key);

        viewModel.set("sev_key", sev_key);
        viewModel.set("cfg", cfg);

        return view("cfg/gateway_edit");

    }

    @XMapping("ajax/save")
    public ViewModel save(String ori_key, String sev_key, String url, String explain) {

        try {

            if (TextUtils.isEmpty(ori_key)) {
                DbWaterApi.addGateway(SEV_CONFIG_TAG, sev_key, url, explain);
            } else {
                DbWaterApi.modGateway(SEV_CONFIG_TAG, ori_key, sev_key, url, explain);
            }

            viewModel.code(1, "成功");

        } catch (Exception e) {
            viewModel.code(0, ThrowableUtils.getString(e));
        }

        return viewModel;

    }

    @XMapping("ajax/enabled")
    public ViewModel enabled(int service_id, int is_enabled) {

        try {

            viewModel.code(1, "成功");

            DbWaterApi.disableService(service_id, is_enabled);

        } catch (Exception e) {
            viewModel.code(0, ThrowableUtils.getString(e));
        }

        return viewModel;

    }

}
