package wateradmin.controller.mot;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthRoles;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.validation.annotation.NotZero;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.SessionRoles;
import wateradmin.dso.SetsUtils;
import wateradmin.dso.TagUtil;
import wateradmin.dso.db.DbWaterCfgGatewayApi;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.dso.db.DbWaterRegApi;
import wateradmin.models.ScaleType;
import wateradmin.models.TagCountsModel;
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
@Mapping("/mot/gw")
public class GwController extends BaseController {

    private static final String SEV_SERVER_TAG = "_service";

    @Mapping("")
    public ModelAndView gateway(Context ctx, String tag_name) throws SQLException {
        if(SetsUtils.waterSettingScale().ordinal() < ScaleType.medium.ordinal()){
            ctx.redirect("/mot/gw/inner");
            return null;
        }

        List<TagCountsModel> tags = DbWaterCfgGatewayApi.getGatewayTagListByEnabled();

        //权限过滤
        BcfTagChecker.filter(tags, m -> m.tag);

        tag_name = TagUtil.build(tag_name, tags);

        viewModel.put("tag_name", tag_name);
        viewModel.put("tags", tags);

        return view("mot/gw");
    }

    @Mapping("inner")
    public ModelAndView inner( String tag_name, int gateway_id) throws SQLException {
        if(SetsUtils.waterSettingScale().ordinal() < ScaleType.medium.ordinal()){
            tag_name = null;
        }

        List<GatewayModel> gats = DbWaterCfgGatewayApi.getGatewayList(tag_name, 1);

        if (gateway_id == 0) {
            if (gats.size() > 0) {
                gateway_id = gats.get(0).gateway_id;
            }
        }

        viewModel.put("tag_name",tag_name);
        viewModel.set("tabs", gats);
        viewModel.set("gateway_id", gateway_id);

        //==================

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

        return view("mot/gw_inner");
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
