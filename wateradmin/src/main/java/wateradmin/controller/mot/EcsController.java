package wateradmin.controller.mot;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import org.noear.water.protocol.MonitorType;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.ETimeType;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.wrap.aliyun.AliyunCmsUtil;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.models.TagCountsModel;
import org.noear.water.protocol.model.EChartModel;
import org.noear.water.protocol.model.ELineModel;
import wateradmin.models.water.ServerTrackEcsModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Mapping("/mot/ecs")
public class EcsController extends BaseController {
    //进入ecs视图
    @Mapping
    public ModelAndView ecs(String tag_name, String name, String sort) throws Exception {
        List<TagCountsModel> tags = DbWaterOpsApi.getServerEcsAccounts();

        viewModel.put("tags", tags);

        if (TextUtils.isEmpty(tag_name) && tags.size() > 0) {
            tag_name = tags.get(0).tag;
        }

        List<ServerTrackEcsModel> list = DbWaterOpsApi.getServerEcsTracks(tag_name, name, sort);

        viewModel.put("tag_name", tag_name);
        viewModel.set("list", list);

        return view("mot/ecs");
    }


    @Mapping("inner")
    public ModelAndView ecs_inner(String instanceId) throws Exception {
        ConfigModel cfg = DbWaterOpsApi.getServerIaasAccount(instanceId);

        if (cfg != null) {
            DescribeInstancesResponse.Instance instance = AliyunCmsUtil.getInstanceInfo(cfg, instanceId);
            int size = AliyunCmsUtil.getEcsDiskInfo(cfg, instanceId).getSize();
            viewModel.set("instance", instance);
            viewModel.set("size", size);

            String attrs = instance.getCpu() + "核/" + (instance.getMemory() / 1000) + "G/" + size + "G";
            DbWaterOpsApi.setServerAttr(instanceId, attrs);
        }

        return view("mot/ecs_inner");
    }


    @Mapping("charts/ajax/reqtate")
    public List<ELineModel> ecs_chart_ajax_reqtate(Integer dateType, Integer dataType, String instanceId) throws Exception {
        if (dataType == null) {
            dataType = 0;
        }
        if (dateType == null) {
            dateType = 0;
        }

        return ProtocolHub.monitoring.query(MonitorType.ECS, instanceId, ETimeType.of(dataType), dataType);
    }

    @Mapping("track/ajax/pull")
    public ViewModel ecs_track_ajax_pull() throws Exception {
        ProtocolHub.monitoring.pull(MonitorType.ECS);

        return viewModel.code(1, "OK");
    }
}
