package wateradmin.controller.mot;

import com.aliyuncs.exceptions.ClientException;
import org.noear.water.protocol.MonitorType;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.ETimeType;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.wrap.aliyun.AliyunBlsUtil;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.models.TagCountsModel;
import org.noear.water.protocol.model.ELineModel;
import wateradmin.models.water.ServerTrackBlsModel;
import wateradmin.models.aliyun.BlsViewModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;


@Controller
@Mapping("/mot/bls")
public class BlsController extends BaseController {

    @Mapping
    public ModelAndView bls(String tag_name, String name, String sort) throws Exception {
        List<TagCountsModel> tags = DbWaterOpsApi.getServerBlsAccounts();

        viewModel.put("tags", tags);

        if (TextUtils.isEmpty(tag_name) && tags.size() > 0) {
            tag_name = tags.get(0).tag;
        }

        List<ServerTrackBlsModel> list = DbWaterOpsApi.getServerBlsTracks(tag_name, name, sort);
        for (ServerTrackBlsModel item : list) {
            item.traffic_tx = (int) (item.traffic_tx / 1000.0);
        }

        viewModel.put("tag_name", tag_name);
        viewModel.set("list", list);


        return view("mot/bls");
    }

    @Mapping("inner")
    public ModelAndView bls_sinner(String instanceId, String name) throws SQLException, ClientException {
        ConfigModel cfg = DbWaterOpsApi.getServerIaasAccount(instanceId);

        if (cfg != null) {
            BlsViewModel blsViewModel = AliyunBlsUtil.getDescribeLoadBalancerAttribute(cfg, instanceId);
            viewModel.set("instanceId", instanceId);
            viewModel.set("instance", blsViewModel);
            viewModel.set("model", blsViewModel);

            DbWaterOpsApi.setServerAttr(instanceId, blsViewModel.loadBalancerSpec);
        }

        return view("mot/bls_inner");
    }


    @Mapping("charts/ajax/reqtate")
    public List<ELineModel> bls_chart_ajax_reqtate(Integer dateType, Integer dataType, String instanceId) throws Exception {
        if (dataType == null) {
            dataType = 0;
        }
        if (dateType == null) {
            dateType = 0;
        }

        return ProtocolHub.monitoring.query(MonitorType.LBS, instanceId, ETimeType.of(dataType), dataType);
    }

    @Mapping("track/ajax/pull")
    public ViewModel bls_track_ajax_pull() throws Exception {

        ProtocolHub.monitoring.pull(MonitorType.LBS);

        return viewModel.code(1, "OK");
    }
}
