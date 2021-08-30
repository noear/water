package wateradmin.controller.cfg;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthRoles;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.NotZero;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ThrowableUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.SessionRoles;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.dso.db.DbWaterRegApi;
import wateradmin.models.water_reg.GatewayVoModel;
import wateradmin.models.water_reg.ServiceConsumerModel;
import wateradmin.models.water_reg.ServiceModel;
import wateradmin.models.water_reg.ServiceSpeedModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@Mapping("/cfg/gateway")
public class GatewayController extends BaseController {

    private static final String SEV_CONFIG_TAG = "_gateway";
    private static final String SEV_SERVER_TAG = "_service";

    @Mapping("")
    public ModelAndView gateway(String tag) throws SQLException {
        List<ConfigModel> sevs = DbWaterCfgApi.getGateways();

        if (TextUtils.isEmpty(tag)) {
            if (sevs.size() > 0) {
                tag = sevs.get(0).key;
            }
        }

        viewModel.set("sevs", sevs);

        viewModel.set("sev_key", tag);

        return view("cfg/gateway");

    }

    @Mapping("inner")
    public ModelAndView inner(String sev_key) throws SQLException {

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

        viewModel.set("is_enabled", cfg.is_enabled);
        viewModel.set("sev_key", sev_key);
        viewModel.set("cfg", cfg.getNode().toData());
        viewModel.set("gtws", gtws);
        viewModel.set("csms", csms);

        return view("cfg/gateway_inner");

    }

    @Mapping("check")
    public String check(String s, String upstream) throws IOException {
        if (TextUtils.isNotEmpty(s) && TextUtils.isNotEmpty(upstream)) {
            if(s.indexOf("@")>0) {
                String ca = s.split("@")[1];
                String url = "http://" + ca + "/run/check/?upstream=" + upstream;
                return HttpUtils.getString(url);
            }
        }

        return "";
    }

    @Mapping("add")
    public ModelAndView add() {
        viewModel.set("is_enabled", 1);
        viewModel.set("cfg", new HashMap<>());

        return view("cfg/gateway_edit");
    }

    @Mapping("edit/{sev_key}")
    public ModelAndView edit(String sev_key) throws SQLException {

        ConfigModel cfg = DbWaterCfgApi.getConfigByTagName(SEV_CONFIG_TAG, sev_key);

        viewModel.set("sev_key", sev_key);
        viewModel.set("is_enabled", cfg.is_enabled);
        viewModel.set("cfg", cfg.getNode().toData());

        return view("cfg/gateway_edit");

    }

    @AuthRoles(SessionRoles.role_admin)
    @NotEmpty("sev_key")
    @Mapping("ajax/save")
    public ViewModel save(String ori_key, String sev_key, String url, String policy, int is_enabled) {

        try {

            if (TextUtils.isEmpty(ori_key)) {
                DbWaterCfgApi.addGateway(SEV_CONFIG_TAG, sev_key, url, policy, is_enabled);
            } else {
                DbWaterCfgApi.updGateway(SEV_CONFIG_TAG, ori_key, sev_key, url, policy, is_enabled);
            }

            viewModel.code(1, "成功");

        } catch (Exception e) {
            viewModel.code(0, ThrowableUtils.getString(e));
        }

        return viewModel;

    }

    @AuthRoles(SessionRoles.role_admin)
    @NotZero("service_id")
    @Mapping("ajax/enabled")
    public ViewModel sev_enabled(long service_id, int is_enabled) {

        try {

            viewModel.code(1, "成功");

            DbWaterRegApi.disableService(service_id, is_enabled);

        } catch (Exception e) {
            viewModel.code(0, ThrowableUtils.getString(e));
        }

        return viewModel;

    }
}
