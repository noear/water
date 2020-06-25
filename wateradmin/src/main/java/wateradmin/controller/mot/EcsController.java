package wateradmin.controller.mot;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.aliyun.AliyunCmsUtil;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.aliyun.AliyunEchartModel;
import wateradmin.models.aliyun.AliyunElineModel;
import wateradmin.models.water.ServerTrackEcsModel;
import wateradmin.models.aliyun.EcsTrackModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@XController
@XMapping("/mot/ecs")
public class EcsController extends BaseController {
    //进入ecs视图
    @XMapping
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


    @XMapping("inner")
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


    @XMapping("charts/ajax/reqtate")
    public List<AliyunElineModel> ecs_chart_ajax_reqtate(Integer dateType, Integer dataType, String instanceId) throws SQLException {
        if (dataType == null) {
            dataType = 0;
        }
        if (dateType == null) {
            dateType = 0;
        }

        ConfigModel cfg = DbWaterOpsApi.getServerIaasAccount(instanceId);

        if (cfg == null) {
            return null;
        }

        AliyunElineModel res = AliyunCmsUtil.baseQuery(cfg, instanceId, dateType, dataType);
        List<AliyunElineModel> rearr = new ArrayList<>();

        if (dataType == 2 || dataType == 4) {
            //增加多线支持
            Map<String, AliyunElineModel> mline = new HashMap<>();

            for (AliyunEchartModel m : res) {
                if (mline.containsKey(m.label) == false) {
                    mline.put(m.label, new AliyunElineModel());
                }

                mline.get(m.label).add(m);
            }

            rearr.addAll(mline.values());
        } else {
            rearr.add(res);
        }

        if (dataType == 3) {
            AliyunElineModel res2 = AliyunCmsUtil.baseQuery(cfg, instanceId, dateType, 5);
            rearr.add(res2);
        }

        return rearr;
    }

    @XMapping("track/ajax/pull")
    public ViewModel ecs_track_ajax_pull() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            if (TextUtils.isEmpty(cfg.value) || cfg.value.indexOf("regionId") < 0) {
                continue;
            }

            List<EcsTrackModel> list = AliyunCmsUtil.pullEcsTrack(cfg);

            DbWaterOpsApi.setServerEcsTracks(list);
        }

        return viewModel.code(1, "OK");
    }
}
