package wateradmin.controller.cfg;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthRoles;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.NotZero;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.SessionRoles;
import wateradmin.dso.db.DbWaterCfgGatewayApi;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.dso.db.DbWaterRegApi;
import wateradmin.models.water_cfg.GatewayModel;
import wateradmin.models.water_reg.GatewayVoModel;
import wateradmin.models.water_reg.ServiceConsumerModel;
import wateradmin.models.water_reg.ServiceModel;
import wateradmin.models.water_reg.ServiceSpeedModel;
import wateradmin.viewModels.ViewModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Mapping("/cfg/gateway")
public class GatewayController extends BaseController {

    private static final String SEV_CONFIG_TAG = "_gateway";
    private static final String SEV_SERVER_TAG = "_service";

    @Mapping("")
    public ModelAndView gateway(int gateway_id) throws SQLException {
        List<GatewayModel> sevs = DbWaterCfgGatewayApi.getGatewayList();

        if (gateway_id == 0) {
            if (sevs.size() > 0) {
                gateway_id = sevs.get(0).gateway_id;
            }
        }

        viewModel.set("sevs", sevs);

        viewModel.set("gateway_id", gateway_id);

        return view("cfg/gateway");

    }

    @Mapping("inner")
    public ModelAndView inner(int gateway_id) throws SQLException {

        GatewayModel cfg = DbWaterCfgGatewayApi.getGateway(gateway_id);

        List<ServiceModel> sevs = DbWaterRegApi.getServicesByName(cfg.name);

        List<ServiceSpeedModel> sevPds = DbWaterOpsApi.getServiceSpeedByService(SEV_SERVER_TAG);

        List<ServiceConsumerModel> csms = DbWaterRegApi.getServiceConsumers(cfg.name);

        List<ServiceSpeedModel> csmPds = DbWaterOpsApi.getServiceSpeedByService("_from", cfg.name);

        List<GatewayVoModel> gtws = new ArrayList<>();

        for (ServiceModel sev : sevs) {
            GatewayVoModel gtw = new GatewayVoModel();
            gtw.service = sev;

            for (ServiceSpeedModel spd : sevPds) {
                if (TextUtils.equals(sev.address, spd.name)) {
                    gtw.speed = spd;
                    break;
                }
            }

            gtws.add(gtw);
        }


        double pdsTotal = 1.00;
        for (ServiceConsumerModel m : csms) {
            for (ServiceSpeedModel spd : csmPds) {
                if (TextUtils.equals(m.consumer + "@" + m.consumer_address, spd.name)) {
                    pdsTotal += spd.total_num;
                    m.traffic_num = spd.total_num;
                    break;
                }
            }
        }

        for (ServiceConsumerModel m : csms) {
            m.traffic_per = (m.traffic_num / pdsTotal) * 100;
        }

        viewModel.set("cfg", cfg);
        viewModel.set("gtws", gtws);
        viewModel.set("csms", csms);

        return view("cfg/gateway_inner");
    }

    @Mapping("check")
    public String check(String s, String upstream) throws IOException {
        if (TextUtils.isNotEmpty(s) && TextUtils.isNotEmpty(upstream)) {
            if (s.indexOf("@") > 0) {
                String ca = s.split("@")[1];
                String url = "http://" + ca + "/run/check/?upstream=" + upstream;
                return HttpUtils.http(url).get();
            }
        }

        return "";
    }

    @Mapping("add")
    public ModelAndView add() {
        GatewayModel cfg = new GatewayModel();
        cfg.is_enabled = 1;

        viewModel.set("cfg", cfg);

        return view("cfg/gateway_edit");
    }

    @Mapping("edit")
    public ModelAndView edit(int gateway_id) throws SQLException {
        GatewayModel cfg = DbWaterCfgGatewayApi.getGateway(gateway_id);

        viewModel.set("cfg", cfg);

        return view("cfg/gateway_edit");

    }

    @AuthRoles(SessionRoles.role_admin)
    @NotEmpty({"tag", "name"})
    @Mapping("ajax/save")
    public ViewModel save(int gateway_id, String tag, String name, String proxy, String policy, int is_enabled) {

        try {
            gateway_id = DbWaterCfgGatewayApi.saveGateway(gateway_id, tag, name, proxy, policy, is_enabled);

            viewModel.put("gateway_id", gateway_id);
            viewModel.code(1, "成功");
        } catch (Exception e) {
            viewModel.code(0, Utils.throwableToString(e));
        }

        return viewModel;

    }

    @AuthRoles(SessionRoles.role_admin)
    @NotZero("service_id")
    @Mapping("ajax/enabled")
    public ViewModel sev_enabled(long service_id, int is_enabled) {
        try {
            DbWaterRegApi.disableService(service_id, is_enabled);

            viewModel.code(1, "成功");
        } catch (Exception e) {
            viewModel.code(0, Utils.throwableToString(e));
        }

        return viewModel;

    }
}
