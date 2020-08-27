package wateradmin.dso.wrap;

import org.noear.water.protocol.Monitoring;
import org.noear.water.protocol.MonitorType;
import org.noear.water.protocol.model.EChartModel;
import org.noear.water.protocol.model.ELineModel;
import org.noear.water.protocol.model.ETimeType;
import org.noear.water.utils.TextUtils;
import wateradmin.dso.wrap.aliyun.AliyunBlsUtil;
import wateradmin.dso.wrap.aliyun.AliyunCmsUtil;
import wateradmin.dso.wrap.aliyun.AliyunDbsUtil;
import wateradmin.dso.db.DbWaterOpsApi;
import wateradmin.models.aliyun.BlsTrackModel;
import wateradmin.models.aliyun.DbsTrackModel;
import wateradmin.models.aliyun.EcsTrackModel;
import wateradmin.models.water_cfg.ConfigModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitoringAliyun implements Monitoring {
    @Override
    public void pull(MonitorType type) throws Exception {
        switch (type) {
            case LBS:
                pull_bls();
            case RDS:
                pull_dbs_rds();
            case Redis:
                pull_dbs_redis();
            case Memcached:
                pull_dbs_mencache();
            case ECS:
                pull_ecs_track();
        }
    }

    @Override
    public List<ELineModel> query(MonitorType type, String instanceId, ETimeType timeType, Integer dataType) throws Exception {
        switch (type) {
            case LBS:
                return query_bls_chart(instanceId, timeType, dataType);
            case ECS:
                return query_ecs_chart(instanceId, timeType, dataType);
            case RDS:
                return query_dbs_chart(instanceId, timeType, dataType, 2);
            case Redis:
                return query_dbs_chart(instanceId, timeType, dataType, 3);
            case Memcached:
                return query_dbs_chart(instanceId, timeType, dataType, 4);
        }

        return null;
    }

    private void pull_bls() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            if (TextUtils.isEmpty(cfg.value) || cfg.value.indexOf("regionId") < 0) {
                continue;
            }

            List<BlsTrackModel> list = AliyunBlsUtil.pullBlsTrack(cfg);

            DbWaterOpsApi.setServerBlsTracks(list);
        }
    }

    private void pull_dbs_rds() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            List<DbsTrackModel> list = AliyunDbsUtil.pullRdsTrack(cfg);

            DbWaterOpsApi.setServerDbsTracks(list);
        }
    }

    private void pull_dbs_redis() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            if (TextUtils.isEmpty(cfg.value) || cfg.value.indexOf("regionId") < 0) {
                continue;
            }

            List<DbsTrackModel> list = AliyunDbsUtil.pullRedisTrack(cfg);

            DbWaterOpsApi.setServerDbsTracks(list);
        }
    }

    private void pull_dbs_mencache() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            if (TextUtils.isEmpty(cfg.value) || cfg.value.indexOf("regionId") < 0) {
                continue;
            }

            List<DbsTrackModel> list = AliyunDbsUtil.pullMemcacheTrack(cfg);

            DbWaterOpsApi.setServerDbsTracks(list);
        }
    }

    private void pull_ecs_track() throws Exception {
        List<ConfigModel> cfgList = DbWaterOpsApi.getIAASAccionts();

        for (ConfigModel cfg : cfgList) {
            if (TextUtils.isEmpty(cfg.value) || cfg.value.indexOf("regionId") < 0) {
                continue;
            }

            List<EcsTrackModel> list = AliyunCmsUtil.pullEcsTrack(cfg);

            DbWaterOpsApi.setServerEcsTracks(list);
        }
    }


    private List<ELineModel> query_bls_chart(String instanceId, ETimeType timeType, Integer dataType) throws Exception {
        if (dataType == null) {
            dataType = 0;
        }

        ConfigModel cfg = DbWaterOpsApi.getServerIaasAccount(instanceId);

        if (cfg == null) {
            return null;
        }


        List<ELineModel> rearr = new ArrayList<>();

        ELineModel res1 = AliyunBlsUtil.baseQuery(cfg, instanceId, timeType, dataType);
        rearr.add(res1);

        if (dataType == 0) { //并发连接
            ELineModel res2 = AliyunBlsUtil.baseQuery(cfg, instanceId, timeType, 5);
            ELineModel res3 = AliyunBlsUtil.baseQuery(cfg, instanceId, timeType, 6);
            rearr.add(res2);
            rearr.add(res3);
        }

        if (dataType == 2) { //QPS
            rearr.clear();

            //增加多线支持
            Map<String, ELineModel> mline = new HashMap<>();

            for (EChartModel m : res1) {
                if (mline.containsKey(m.label) == false) {
                    mline.put(m.label, new ELineModel());
                }

                mline.get(m.label).add(m);
            }

            rearr.addAll(mline.values());
        }

        if (dataType == 3) { //流量
            ELineModel res2 = AliyunBlsUtil.baseQuery(cfg, instanceId, timeType.code, 4);
            rearr.add(res2);
        }

        return rearr;
    }

    private List<ELineModel> query_dbs_chart(String instanceId, ETimeType timeType, Integer dataType, Integer type) throws Exception {
        if (dataType == null) {
            dataType = 0;
        }

        if (type == null) {
            type = 0;
        }

        List<ELineModel> lines  =new ArrayList<>();

        ELineModel res = new ELineModel();

        ConfigModel cfg = DbWaterOpsApi.getServerIaasAccount(instanceId);

        if (cfg != null) {
            res = AliyunDbsUtil.baseQuery(cfg, instanceId, timeType, dataType, type);
        }

        lines.add(res);

        return lines;
    }

    private List<ELineModel> query_ecs_chart(String instanceId, ETimeType timeType, Integer dataType) throws SQLException {
        if (dataType == null) {
            dataType = 0;
        }

        ConfigModel cfg = DbWaterOpsApi.getServerIaasAccount(instanceId);

        if (cfg == null) {
            return null;
        }

        ELineModel res = AliyunCmsUtil.baseQuery(cfg, instanceId, timeType, dataType);
        List<ELineModel> rearr = new ArrayList<>();

        if (dataType == 2 || dataType == 4) {
            //增加多线支持
            Map<String, ELineModel> mline = new HashMap<>();

            for (EChartModel m : res) {
                if (mline.containsKey(m.label) == false) {
                    mline.put(m.label, new ELineModel());
                }

                mline.get(m.label).add(m);
            }

            rearr.addAll(mline.values());
        } else {
            rearr.add(res);
        }

        if (dataType == 3) {
            ELineModel res2 = AliyunCmsUtil.baseQuery(cfg, instanceId, timeType, 5);
            rearr.add(res2);
        }

        return rearr;
    }
}
