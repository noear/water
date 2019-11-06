package org.noear.water.admin.plugin_aliyun.dso.db;

import org.noear.water.admin.plugin_aliyun.Config;
import org.noear.water.admin.plugin_aliyun.model.water.ConfigModel;
import org.noear.water.admin.plugin_aliyun.model.aliyun.BlsTrackModel;
import org.noear.water.admin.plugin_aliyun.model.aliyun.DbsTrackModel;
import org.noear.water.admin.plugin_aliyun.model.aliyun.EcsTrackModel;
import org.noear.water.admin.plugin_aliyun.model.water_wind.WindServerModel;
import org.noear.water.tools.TextUtils;
import org.noear.weed.DbContext;

import java.sql.SQLException;
import java.util.List;

public class DbWindApi {
    public static DbContext db(){
        return Config.db;
    }

    public static List<ConfigModel> getIAASAccionts() throws SQLException {
        return DbWaterApi.getConfigByType(null, 1001);//1001=阿里云账号
    }

    public static WindServerModel getServerByIAAS(String iaas_key) throws SQLException {
        return db().table("wind_server")
                .where("iaas_key = ?", iaas_key)
                .limit(1)
                .select("*")
                .getItem(new WindServerModel());
    }


    public static ConfigModel getServerIaasAccount(String iaas_key) throws SQLException {
        WindServerModel sm = getServerByIAAS(iaas_key);
        if (sm == null || TextUtils.isEmpty(sm.iaas_account)) {
            return null;
        }

        ConfigModel cfg = DbWaterApi.getConfigByTagName(sm.iaas_account);

        if (TextUtils.isEmpty(cfg.value)) {
            return null;
        } else {
            return cfg;
        }
    }

    public static void setServerEcsTracks(List<EcsTrackModel> list) throws Exception {
        for (EcsTrackModel m : list) {
            db().table("wind_server_track_ecs").usingExpr(true).log(false)
                    .set("iaas_key", m.instanceId)
                    .set("cpu_usage", m.cpu)
                    .set("memory_usage", m.memory)
                    .set("disk_usage", m.disk)
                    .set("broadband_usage", m.broadband)
                    .set("tcp_num", m.tcp)
                    .set("last_updatetime", "$NOW()")
                    .updateExt("iaas_key");
        }
    }

    public static void setServerBlsTracks(List<BlsTrackModel> list) throws Exception {
        for (BlsTrackModel m : list) {
            db().table("wind_server_track_bls").usingExpr(true).log(false)
                    .set("iaas_key", m.instanceId)
                    .set("co_conect_num", m.co_conect_num)
                    .set("new_conect_num", m.new_conect_num)
                    .set("qps", m.qps)
                    .set("traffic_tx", m.traffic_tx)
                    .set("last_updatetime", "$NOW()")
                    .updateExt("iaas_key");
        }
    }

    public static void setServerDbsTracks(List<DbsTrackModel> list) throws Exception {
        for (DbsTrackModel m : list) {
            db().table("wind_server_track_dbs").usingExpr(true).log(false)
                    .set("iaas_key", m.instanceId)
                    .set("connect_usage", m.connect_usage)
                    .set("cpu_usage", m.cpu_usage)
                    .set("memory_usage", m.memory_usage)
                    .set("disk_usage", m.disk_usage)
                    .set("last_updatetime", "$NOW()")
                    .updateExt("iaas_key");
        }
    }
}
