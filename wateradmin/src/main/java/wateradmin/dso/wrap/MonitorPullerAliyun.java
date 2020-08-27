package wateradmin.dso.wrap;

import org.noear.water.protocol.MonitorPuller;
import org.noear.water.protocol.MonitorType;
import org.noear.water.utils.TextUtils;
import wateradmin.dso.wrap.aliyun.AliyunBlsUtil;
import wateradmin.dso.wrap.aliyun.AliyunCmsUtil;
import wateradmin.dso.wrap.aliyun.AliyunDbsUtil;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.models.aliyun.BlsTrackModel;
import wateradmin.models.aliyun.DbsTrackModel;
import wateradmin.models.aliyun.EcsTrackModel;
import wateradmin.models.water_cfg.ConfigModel;

import java.util.List;

public class MonitorPullerAliyun implements MonitorPuller {
    @Override
    public void pull(MonitorType type) throws Exception {
        switch (type) {
            case LBS:
                bls_track_ajax_pull();
            case RDS:
                dbs_rds_track_ajax_pull();
            case Redis:
                dbs_redis_track_ajax_pull();
            case Memcached:
                dbs_mencache_track_ajax_pull();
            case ECS:
                ecs_track_ajax_pull();
        }
    }

    private void bls_track_ajax_pull() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            if (TextUtils.isEmpty(cfg.value) || cfg.value.indexOf("regionId") < 0) {
                continue;
            }

            List<BlsTrackModel> list = AliyunBlsUtil.pullBlsTrack(cfg);

            DbWaterOpsApi.setServerBlsTracks(list);
        }
    }


    private void dbs_rds_track_ajax_pull() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            List<DbsTrackModel> list = AliyunDbsUtil.pullRdsTrack(cfg);

            DbWaterOpsApi.setServerDbsTracks(list);
        }
    }

    private void dbs_redis_track_ajax_pull() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            if (TextUtils.isEmpty(cfg.value) || cfg.value.indexOf("regionId") < 0) {
                continue;
            }

            List<DbsTrackModel> list = AliyunDbsUtil.pullRedisTrack(cfg);

            DbWaterOpsApi.setServerDbsTracks(list);
        }
    }

    private void dbs_mencache_track_ajax_pull() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            if (TextUtils.isEmpty(cfg.value) || cfg.value.indexOf("regionId") < 0) {
                continue;
            }

            List<DbsTrackModel> list = AliyunDbsUtil.pullMemcacheTrack(cfg);

            DbWaterOpsApi.setServerDbsTracks(list);
        }
    }

    private void ecs_track_ajax_pull() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            if (TextUtils.isEmpty(cfg.value) || cfg.value.indexOf("regionId") < 0) {
                continue;
            }

            List<EcsTrackModel> list = AliyunCmsUtil.pullEcsTrack(cfg);

            DbWaterOpsApi.setServerEcsTracks(list);
        }
    }
}
