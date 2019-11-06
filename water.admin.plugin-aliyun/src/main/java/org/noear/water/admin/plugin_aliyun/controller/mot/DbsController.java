package org.noear.water.admin.plugin_aliyun.controller.mot;

import com.aliyuncs.exceptions.ClientException;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.plugin_aliyun.dso.AliyunDbsUtil;
import org.noear.water.admin.plugin_aliyun.dso.db.DbWindApi;
import org.noear.water.admin.plugin_aliyun.model.aliyun.AliyunEchartModel;
import org.noear.water.admin.plugin_aliyun.model.aliyun.AliyunElineModel;
import org.noear.water.admin.plugin_aliyun.model.aliyun.DbsTrackModel;
import org.noear.water.admin.plugin_aliyun.model.aliyun.DbsViewModel;
import org.noear.water.admin.plugin_aliyun.model.water.ConfigModel;
import org.noear.water.admin.plugin_aliyun.model.water.ServerTrackDbsModel;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;

import java.sql.SQLException;
import java.util.List;


@XController
@XMapping("/mot/")
public class DbsController extends BaseController {

    @XMapping("dbs")
    public ModelAndView dbs(String tag_name, String name, String sort) throws Exception {
        List<ConfigModel> tags = DbWindApi.getServerDbsAccounts();

        viewModel.put("tags",tags);

        if (TextUtils.isEmpty(tag_name) && tags.size()>0) {
            tag_name = tags.get(0).tag;
        }

        List<ServerTrackDbsModel> list = DbWindApi.getServerDbsTracks(tag_name,name, sort);

        viewModel.put("tag_name",tag_name);
        viewModel.set("list", list);

        return view("mot/dbs");
    }


    @XMapping("dbs/inner")
    public ModelAndView dbs_sinner(String id, String type, String name) throws SQLException, ClientException {
        DbsViewModel model = null;

        ConfigModel cfg = DbWindApi.getServerIaasAccount(id);

        if (cfg != null) {
            switch (type) {
                case "2":
                    model = AliyunDbsUtil.getDescribeDbAttribute(cfg, id);
                    break;  //RDS
                case "3":
                    model = AliyunDbsUtil.getKvInstance(cfg, id, "3");
                    break;        //redis
                case "4":
                    model = AliyunDbsUtil.getKvInstance(cfg, id, "4");
                    break;       //memcache
//                case "5":
//
//                    break;       //drds
                default:
                    model = AliyunDbsUtil.getDescribeDbAttribute(cfg, id);
                    break;
            }
        }

        viewModel.set("model", model);
        viewModel.set("id", id);
        viewModel.set("type", type);
        viewModel.set("name", name);
        return view("mot/dbs_inner");
    }


    @XMapping("dbs/charts/ajax/reqtate")
    public List<AliyunEchartModel> dbs_chart_ajax_reqtate(Integer dateType, Integer dataType, String inId, Integer type) throws Exception {
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

        ConfigModel cfg = DbWindApi.getServerIaasAccount(inId);

        if (cfg != null) {
            res = AliyunDbsUtil.baseQuery(cfg, inId, dateType, dataType, type);
        }

        return res;
    }

    @XMapping("dbs/track/ajax/pull")
    public ViewModel dbs_track_ajax_pull() throws Exception {
        List<ConfigModel> cfgList = DbWindApi.getIAASAccionts();

        for(ConfigModel cfg : cfgList) {
            List<DbsTrackModel> list = AliyunDbsUtil.pullRdsTrack(cfg);

            DbWindApi.setServerDbsTracks(list);
        }

        return viewModel.code(1, "OK");
    }


    @XMapping("dbs/redis/track/ajax/pull")
    public ViewModel dbs_redis_track_ajax_pull() throws Exception {
        List<ConfigModel> cfgList = DbWindApi.getIAASAccionts();

        for(ConfigModel cfg : cfgList) {
            List<DbsTrackModel> list = AliyunDbsUtil.pullRedisTrack(cfg);

            DbWindApi.setServerDbsTracks(list);
        }

        return viewModel.code(1, "OK");
    }

    @XMapping("dbs/mencache/track/ajax/pull")
    public ViewModel dbs_mencache_track_ajax_pull() throws Exception {
        List<ConfigModel> cfgList = DbWindApi.getIAASAccionts();

        for(ConfigModel cfg : cfgList) {
            List<DbsTrackModel> list = AliyunDbsUtil.pullMemcacheTrack(cfg);

            DbWindApi.setServerDbsTracks(list);
        }

        return viewModel.code(1, "OK");
    }
}
