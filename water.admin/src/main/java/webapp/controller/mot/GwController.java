package webapp.controller.mot;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.tools.StringUtils;
import org.noear.water.tools.TextUtils;
import webapp.dao.db.DbWaterApi;
import webapp.dao.db.DbWindApi;
import webapp.models.water_dev.GatewayVoModel;
import webapp.models.water.ConfigModel;
import webapp.models.water.ServiceConsumerModel;
import webapp.models.water.ServiceModel;
import webapp.models.water.ServiceSpeedModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@XController
@XMapping("/mot/gw")
public class GwController extends BaseController {

    private static final String SEV_CONFIG_TAG = "_service";

    @XMapping("")
    public ModelAndView gw() throws SQLException {

        List<ConfigModel> sevs = DbWaterApi.getGateways();

        viewModel.set("sevs", sevs);

        viewModel.set("sev_key", sevs.size() > 0 ? sevs.get(0).key : null);

        return view("mot/gw");

    }

    @XMapping("inner")
    public ModelAndView gw_inner(String sev_key) throws SQLException {

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
                if (StringUtils.equals(sev.address, spd.name)) {
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

        return view("mot/gw_inner");

    }
}
