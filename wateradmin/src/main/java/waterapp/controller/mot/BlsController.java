package waterapp.controller.mot;

import com.aliyuncs.exceptions.ClientException;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import waterapp.controller.BaseController;
import waterapp.dso.aliyun.AliyunBlsUtil;
import waterapp.dso.db.DbWaterOpsApi;
import waterapp.models.TagCountsModel;
import waterapp.models.aliyun.AliyunEchartModel;
import waterapp.models.aliyun.AliyunElineModel;
import waterapp.models.aliyun.BlsTrackModel;
import waterapp.models.aliyun.BlsViewModel;
import waterapp.models.water_cfg.ConfigModel;
import waterapp.models.water.ServerTrackBlsModel;
import waterapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@XController
@XMapping("/mot/")
public class BlsController extends BaseController {

    @XMapping("bls")
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

    @XMapping("bls/inner")
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


    @XMapping("bls/charts/ajax/reqtate")
    public List<AliyunElineModel> bls_chart_ajax_reqtate(Integer dateType, Integer dataType, String instanceId) throws Exception {
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


        List<AliyunElineModel> rearr = new ArrayList<>();

        AliyunElineModel res1 = AliyunBlsUtil.baseQuery(cfg, instanceId, dateType, dataType);
        rearr.add(res1);

        if (dataType == 0) { //并发连接
            AliyunElineModel res2 = AliyunBlsUtil.baseQuery(cfg, instanceId, dateType, 5);
            AliyunElineModel res3 = AliyunBlsUtil.baseQuery(cfg, instanceId, dateType, 6);
            rearr.add(res2);
            rearr.add(res3);
        }

        if (dataType == 2) { //QPS
            rearr.clear();

            //增加多线支持
            Map<String, AliyunElineModel> mline = new HashMap<>();

            for (AliyunEchartModel m : res1) {
                if (mline.containsKey(m.label) == false) {
                    mline.put(m.label, new AliyunElineModel());
                }

                mline.get(m.label).add(m);
            }

            rearr.addAll(mline.values());
        }

        if (dataType == 3) { //流量
            AliyunElineModel res2 = AliyunBlsUtil.baseQuery(cfg, instanceId, dateType, 4);
            rearr.add(res2);
        }

        return rearr;
    }

    @XMapping("bls/track/ajax/pull")
    public ViewModel bls_track_ajax_pull() throws Exception {

        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            if (TextUtils.isEmpty(cfg.value) || cfg.value.indexOf("regionId") < 0) {
                continue;
            }

            List<BlsTrackModel> list = AliyunBlsUtil.pullBlsTrack(cfg);

            DbWaterOpsApi.setServerBlsTracks(list);
        }

        return viewModel.code(1, "OK");
    }
}
