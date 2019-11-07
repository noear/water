package org.noear.water.admin.plugin_aliyun.dso.db;

import org.noear.water.admin.plugin_aliyun.Config;
import org.noear.water.admin.plugin_aliyun.model.water.ConfigModel;
import org.noear.water.admin.plugin_aliyun.model.aliyun.BlsTrackModel;
import org.noear.water.admin.plugin_aliyun.model.aliyun.DbsTrackModel;
import org.noear.water.admin.plugin_aliyun.model.aliyun.EcsTrackModel;
import org.noear.water.admin.plugin_aliyun.model.water.ServerTrackBlsModel;
import org.noear.water.admin.plugin_aliyun.model.water.ServerTrackDbsModel;
import org.noear.water.admin.plugin_aliyun.model.water.ServerTrackEcsModel;
import org.noear.water.admin.plugin_aliyun.model.water_wind.WindServerModel;
import org.noear.water.admin.tools.dso.CacheUtil;
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

    public static List<ServerTrackEcsModel> getServerEcsTracks(String tag, String name, String sort) throws Exception {
        return db().table("wind_server s")
                .leftJoin("wind_server_track_ecs t").on("s.iaas_type=0 AND s.is_enabled=1 AND s.iaas_key = t.iaas_key")
                .where("s.iaas_type=0 AND s.is_enabled=1")
                .and("s.iaas_account = ?", "wind/" + tag)
                .expre((tb) -> {
                    if (TextUtils.isEmpty(name) == false) {
                        tb.and("s.name like ?", "%" + name + "%");
                    }

                    if (TextUtils.isEmpty(sort) == false) {
                        if ("sev_num".equals(sort)) {
                            tb.orderBy("s." + sort + " DESC");
                        } else {
                            tb.orderBy("t." + sort + " DESC");
                        }
                    } else {
                        tb.orderBy("s.tag ASC,s.name ASC");
                    }
                })
                .select("s.server_id,s.name,s.tag,s.iaas_type,s.iaas_key,s.sev_num,s.address_local,t.*")
                .getList(new ServerTrackEcsModel());
    }



    public static List<ServerTrackBlsModel> getServerBlsTracks(String tag, String name, String sort) throws Exception {
        return db().table("wind_server s")
                .leftJoin("wind_server_track_bls t").on("s.iaas_type=1 AND s.is_enabled=1 AND s.iaas_key = t.iaas_key")
                .where("s.iaas_type=1 AND s.is_enabled=1")
                .and("s.iaas_account = ?", "wind/" + tag)
                .expre((tb) -> {

                    if (TextUtils.isEmpty(name) == false) {
                        tb.and("s.name like ?", "%" + name + "%");
                    }

                    if (TextUtils.isEmpty(sort) == false) {
                        tb.orderBy("t." + sort + " DESC");
                    } else {
                        tb.orderBy("s.tag ASC,s.name ASC");
                    }
                })
                .select("s.server_id,s.name,s.tag,s.iaas_type,s.iaas_key,t.*")
                .getList(new ServerTrackBlsModel());
    }

    public static List<ServerTrackDbsModel> getServerDbsTracks(String tag, String name, String sort) throws Exception {
        return db().table("wind_server s")
                .leftJoin("wind_server_track_dbs t").on("s.iaas_type>=2 AND s.is_enabled=1 AND s.iaas_key = t.iaas_key")
                .where("s.iaas_type>=2 AND s.is_enabled=1")
                .and("s.iaas_account = ?", "wind/" + tag)
                .expre((tb) -> {
                    if (TextUtils.isEmpty(name) == false) {
                        tb.and("s.name like ?", "%" + name + "%");
                    }

                    if (TextUtils.isEmpty(sort) == false) {
                        tb.orderBy("t." + sort + " DESC");
                    } else {
                        tb.orderBy("s.tag ASC,s.name ASC");
                    }
                })
                .select("s.server_id,s.name,s.tag,s.iaas_type,s.iaas_key,t.*")
                .getList(new ServerTrackDbsModel());
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

    public static List<ConfigModel> getServerBlsAccounts() throws SQLException {
        return db().table("wind_server")
                .where("iaas_type=1 AND is_enabled=1")
                .groupBy("iaas_account")
                .select("replace(iaas_account,'wind/','') tag, COUNT(iaas_account) counts")
                .caching(CacheUtil.data)
                .getList(new ConfigModel());
    }

    public static List<ConfigModel> getServerDbsAccounts() throws SQLException {
        return db().table("wind_server")
                .where("iaas_type>=2 AND is_enabled=1")
                .groupBy("iaas_account")
                .select("replace(iaas_account,'wind/','') tag, COUNT(iaas_account) counts")
                .caching(CacheUtil.data)
                .getList(new ConfigModel());
    }

    public static List<ConfigModel> getServerEcsAccounts() throws SQLException {
        return db().table("wind_server")
                .where("iaas_type=0 AND is_enabled=1")
                .groupBy("iaas_account")
                .select("replace(iaas_account,'wind/','') tag, COUNT(iaas_account) counts")
                .caching(CacheUtil.data)
                .getList(new ConfigModel());
    }
}
