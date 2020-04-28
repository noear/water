package waterapp.controller.mot;

import com.aliyuncs.exceptions.ClientException;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import waterapp.controller.BaseController;
import waterapp.dso.aliyun.AliyunDbsUtil;
import waterapp.dso.db.DbWaterOpsApi;
import waterapp.models.TagCountsModel;
import waterapp.models.aliyun.AliyunEchartModel;
import waterapp.models.aliyun.AliyunElineModel;
import waterapp.models.aliyun.DbsTrackModel;
import waterapp.models.aliyun.DbsViewModel;
import waterapp.models.water_cfg.ConfigModel;
import waterapp.models.water.ServerTrackDbsModel;
import waterapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;


@XController
@XMapping("/mot/")
public class DbsController extends BaseController {

    @XMapping("dbs")
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


    @XMapping("dbs/inner")
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


    @XMapping("dbs/charts/ajax/reqtate")
    public List<AliyunEchartModel> dbs_chart_ajax_reqtate(Integer dateType, Integer dataType, String instanceId, Integer type) throws Exception {
        if (dataType == null) {
            dataType = 0;
        }
        if (dateType == null) {
            dateType = 0;
        }
        if (type == null) {
            type = 0;
        }

        AliyunElineModel res = new AliyunElineModel();

        ConfigModel cfg = DbWaterOpsApi.getServerIaasAccount(instanceId);

        if (cfg != null) {
            res = AliyunDbsUtil.baseQuery(cfg, instanceId, dateType, dataType, type);
        }

        return res;
    }

    @XMapping("dbs/track/ajax/pull")
    public ViewModel dbs_track_ajax_pull() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            List<DbsTrackModel> list = AliyunDbsUtil.pullRdsTrack(cfg);

            DbWaterOpsApi.setServerDbsTracks(list);
        }

        return viewModel.code(1, "OK");
    }


    @XMapping("dbs/redis/track/ajax/pull")
    public ViewModel dbs_redis_track_ajax_pull() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            List<DbsTrackModel> list = AliyunDbsUtil.pullRedisTrack(cfg);

            DbWaterOpsApi.setServerDbsTracks(list);
        }

        return viewModel.code(1, "OK");
    }

    @XMapping("dbs/mencache/track/ajax/pull")
    public ViewModel dbs_mencache_track_ajax_pull() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            List<DbsTrackModel> list = AliyunDbsUtil.pullMemcacheTrack(cfg);

            DbWaterOpsApi.setServerDbsTracks(list);
        }

        return viewModel.code(1, "OK");
    }
}
