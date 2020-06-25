package wateradmin.controller.mot;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.dso.db.DbWaterRegApi;
import wateradmin.models.water_reg.GatewayVoModel;
import wateradmin.models.water_reg.ServiceConsumerModel;
import wateradmin.models.water_reg.ServiceModel;
import wateradmin.models.water_reg.ServiceSpeedModel;
import wateradmin.models.water_cfg.ConfigModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@XController
@XMapping("/mot/gw")
public class GwController extends BaseController {

    private static final String SEV_CONFIG_TAG = "_gateway";
    private static final String SEV_SERVER_TAG = "_service";

    @XMapping("")
    public ModelAndView gw() throws SQLException {
        List<ConfigModel> sevs = DbWaterCfgApi.getGateways();

        viewModel.set("sevs", sevs);

        viewModel.set("sev_key", sevs.size() > 0 ? sevs.get(0).key : null);

        return view("mot/gw");

    }

    @XMapping("inner")
    public ModelAndView gw_inner(String sev_key) throws SQLException {

        ConfigModel cfg = DbWaterCfgApi.getConfigByTagName(SEV_CONFIG_TAG, sev_key);
        String sev_tmp = cfg.getProp().getProperty("service");

        if(TextUtils.isEmpty(sev_tmp)==false){ //通过 cfg.user, 实现别名与实名的情况
            sev_key = sev_tmp;
        }

        double pdsTotal = 0.01;

        List<ServiceModel> sevs = DbWaterRegApi.getServicesByName(sev_key);

        List<ServiceSpeedModel> sevPds = DbWaterOpsApi.getServiceSpeedByService(SEV_SERVER_TAG);

        List<ServiceConsumerModel> csms = DbWaterRegApi.getServiceConsumers(sev_key);

        List<ServiceSpeedModel> csmPds = DbWaterOpsApi.getServiceSpeedByService("_from",sev_key);

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
        viewModel.set("cfg", cfg.getNode().toData());
        viewModel.set("gtws", gtws);
        viewModel.set("csms", csms);

        return view("mot/gw_inner");

    }
}
