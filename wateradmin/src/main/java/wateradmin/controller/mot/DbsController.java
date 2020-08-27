package wateradmin.controller.mot;

import com.aliyuncs.exceptions.ClientException;
import org.noear.water.protocol.MonitorType;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.wrap.aliyun.AliyunDbsUtil;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.models.TagCountsModel;
import org.noear.water.protocol.model.EChartModel;
import org.noear.water.protocol.model.ELineModel;
import wateradmin.models.water.ServerTrackDbsModel;
import wateradmin.models.aliyun.DbsViewModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@XController
@XMapping("/mot/dbs")
public class DbsController extends BaseController {

    @XMapping
    public ModelAndView dbs(String tag_name, String name, String sort) throws Exception {
        List<TagCountsModel> tags = DbWaterOpsApi.getServerDbsAccounts();

        viewModel.put("tags", tags);

        if (TextUtils.isEmpty(tag_name) && tags.size() > 0) {
            tag_name = tags.get(0).tag;
        }

        List<ServerTrackDbsModel> list = DbWaterOpsApi.getServerDbsTracks(tag_name, name, sort);

        viewModel.put("tag_name", tag_name);
        viewModel.set("list", list);

        return view("mot/dbs");
    }


    @XMapping("inner")
    public ModelAndView dbs_sinner(String instanceId, String type, String name) throws SQLException, ClientException {
        DbsViewModel instance = null;

        ConfigModel cfg = DbWaterOpsApi.getServerIaasAccount(instanceId);

        if (cfg != null) {
            switch (type) {
                case "2":
                    instance = AliyunDbsUtil.getDescribeDbAttribute(cfg, instanceId);
                    break;  //RDS
                case "3":
                    instance = AliyunDbsUtil.getKvInstance(cfg, instanceId, "3");
                    break;        //redis
                case "4":
                    instance = AliyunDbsUtil.getKvInstance(cfg, instanceId, "4");
                    break;       //memcache
//                case "5":
//
//                    break;       //drds
                default:
                    instance = AliyunDbsUtil.getDescribeDbAttribute(cfg, instanceId);
                    break;
            }
        }

        viewModel.set("instance", instance);
        viewModel.set("instanceId", instanceId);
        viewModel.set("type", type);
        viewModel.set("name", name);
        
        DbWaterOpsApi.setServerAttr(instanceId, instance.productType());

        return view("mot/dbs_inner");
    }


    @XMapping("charts/ajax/reqtate")
    public List<ELineModel> dbs_chart_ajax_reqtate(Integer dateType, Integer dataType, String instanceId, Integer type) throws Exception {
        if (dataType == null) {
            dataType = 0;
        }
        if (dateType == null) {
            dateType = 0;
        }
        if (type == null) {
            type = 0;
        }

        List<ELineModel> lines  =new ArrayList<>();

        ELineModel res = new ELineModel();

        ConfigModel cfg = DbWaterOpsApi.getServerIaasAccount(instanceId);

        if (cfg != null) {
            res = AliyunDbsUtil.baseQuery(cfg, instanceId, dateType, dataType, type);
        }

        lines.add(res);

        return lines;
    }

    @XMapping("track/ajax/pull")
    public ViewModel dbs_rds_track_ajax_pull() throws Exception {
        ProtocolHub.monitorPuller.pull(MonitorType.RDS);

        return viewModel.code(1, "OK");
    }


    @XMapping("redis/track/ajax/pull")
    public ViewModel dbs_redis_track_ajax_pull() throws Exception {
        ProtocolHub.monitorPuller.pull(MonitorType.Redis);

        return viewModel.code(1, "OK");
    }

    @XMapping("mencache/track/ajax/pull")
    public ViewModel dbs_mencache_track_ajax_pull() throws Exception {
        ProtocolHub.monitorPuller.pull(MonitorType.Memcached);

        return viewModel.code(1, "OK");
    }
}
