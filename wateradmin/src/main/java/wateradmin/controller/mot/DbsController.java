package wateradmin.controller.mot;

import com.aliyuncs.exceptions.ClientException;
import org.noear.water.protocol.MonitorType;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.protocol.model.monitor.ETimeType;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.wrap.aliyun.AliyunDbsUtil;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.models.TagCountsModel;
import org.noear.water.protocol.model.monitor.ELineModel;
import wateradmin.models.water.ServerTrackDbsModel;
import wateradmin.models.aliyun.DbsViewModel;
import wateradmin.models.water_cfg.ConfigModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;


@Controller
@Mapping("/mot/dbs")
public class DbsController extends BaseController {

    @Mapping
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


    @Mapping("inner")
    public ModelAndView dbs_sinner(String instanceId, String type, String name) throws SQLException, ClientException {
        ConfigModel cfg = DbWaterOpsApi.getServerIaasAccount(instanceId);

        if (cfg != null && TextUtils.isNotEmpty(cfg.value)) {
            DbsViewModel instance = null;
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

            viewModel.set("instance", instance);
            viewModel.set("instanceId", instanceId);
            viewModel.set("type", type);
            viewModel.set("name", name);

            DbWaterOpsApi.setServerAttr(instanceId, instance.productType());

            return view("mot/dbs_inner");
        } else {
            throw new RuntimeException("There is no iaas.ram configuration");
        }
    }


    @Mapping("charts/ajax/reqtate")
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

        if (type == 2) {
            return ProtocolHub.monitoring.query(MonitorType.RDS, instanceId, ETimeType.of(dateType), dataType);
        } else if (type == 3) {
            return ProtocolHub.monitoring.query(MonitorType.Redis, instanceId, ETimeType.of(dateType), dataType);
        } else if (type == 4) {
            return ProtocolHub.monitoring.query(MonitorType.Memcached, instanceId, ETimeType.of(dateType), dataType);
        } else {
            return null;
        }
    }

    @Mapping("track/ajax/pull")
    public ViewModel dbs_rds_track_ajax_pull() throws Exception {
        ProtocolHub.monitoring.pull(MonitorType.RDS);
        ProtocolHub.monitoring.pull(MonitorType.PolarDB);

        return viewModel.code(1, "OK");
    }


    @Mapping("redis/track/ajax/pull")
    public ViewModel dbs_redis_track_ajax_pull() throws Exception {
        ProtocolHub.monitoring.pull(MonitorType.Redis);

        return viewModel.code(1, "OK");
    }

    @Mapping("mencache/track/ajax/pull")
    public ViewModel dbs_mencache_track_ajax_pull() throws Exception {
        ProtocolHub.monitoring.pull(MonitorType.Memcached);

        return viewModel.code(1, "OK");
    }
}
