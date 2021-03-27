package wateradmin.dso.db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.noear.water.utils.Datetime;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import org.noear.water.utils.TextUtils;
import wateradmin.Config;
import wateradmin.dso.CacheUtil;
import wateradmin.models.water.ServerTrackBlsModel;
import wateradmin.models.water.ServerTrackDbsModel;
import wateradmin.models.water.ServerTrackEcsModel;
import wateradmin.models.water_ops.ServerModel;
import wateradmin.models.water_reg.ServiceSpeedDateModel;
import wateradmin.models.water_reg.ServiceSpeedHourModel;
import wateradmin.models.water_reg.ServiceSpeedModel;
import wateradmin.models.TagCountsModel;
import wateradmin.models.aliyun.BlsTrackModel;
import wateradmin.models.aliyun.DbsTrackModel;
import wateradmin.models.aliyun.EcsTrackModel;
import wateradmin.models.water_cfg.ConfigModel;

import java.sql.SQLException;
import java.util.*;

public class DbWaterOpsApi {

    private static DbContext db() {
        return Config.water;
    }

    ////////////////////////////////////////////////////

    //根据标签名称和状态获取服务列表。
    public static List<ServerModel> getServerByTagNameAndState(String tag, int is_enabled) throws SQLException {
        return db().table("water_ops_server")
                   .where("tag = ?", tag)
                   .and("is_enabled = ?", is_enabled)
                   .select("*")
                   .getList(new ServerModel());
    }


    //获取服务标签
    public static List<ServerModel> getServerTags() throws SQLException {
        return db().table("water_ops_server")
                   .groupBy("tag")
                   .orderByAsc("tag")
                   .select("tag,count(*) counts")
                   .getList(new ServerModel());
    }

    //禁用/启用 服务
    public static boolean disableServer(int server_id, int is_enabled) throws SQLException {
        return db().table("water_ops_server")
                   .where("server_id = ?", server_id)
                   .set("is_enabled", is_enabled)
                   .update() > 0;
    }

    public static boolean deleteServer(int server_id) throws SQLException {
        return db().table("water_ops_server")
                .where("server_id = ?", server_id)
                .delete() > 0;
    }

    public static boolean addServer(String tag, String name, String ip, String hosts, String note, int is_enabled, int env_type) throws SQLException {
        return db().table("water_ops_server")
                   .set("tag", tag)
                   .set("name", name)
                   .set("ip", ip)
                   .set("hosts", hosts)
                   .set("is_enabled", is_enabled)
                   .set("env_type", env_type)
                   .insert() > 0;
    }

    public static List<ConfigModel> getIAASAccionts() throws SQLException {
        return DbWaterCfgApi.getConfigsByType(null, 1003);//1003=阿里云IAAS账号
    }

    public static List<TagCountsModel> getServerBlsAccounts() throws SQLException {
        return getServerAccounts("iaas_type=1");
    }

    public static List<TagCountsModel> getServerDbsAccounts() throws SQLException {
        return getServerAccounts("iaas_type IN (2,3,4,5,7,8)");
    }

    public static List<TagCountsModel> getServerEcsAccounts() throws SQLException {
        return getServerAccounts("iaas_type=0");
    }

    private static List<TagCountsModel> getServerAccounts(String where) throws SQLException {
        List<TagCountsModel> list = db().table("water_ops_server")
                .where(where).and("is_enabled=1").andNeq("iaas_account", "")
                .groupBy("iaas_account")
                .select("iaas_account tag, COUNT(iaas_account) counts")
                .caching(CacheUtil.data)
                .getList(TagCountsModel.class);

        for (TagCountsModel m : list) {

            int idx = m.tag.indexOf("/");
            if (idx > 0) {
                m.note = m.tag.substring(idx + 1);
            } else {
                m.note = m.tag;
            }
        }

        return list;
    }

    public static ServerModel getServerByID(int server_id) throws SQLException {
        return db().table("water_ops_server")
                   .where("server_id = ?", server_id)
                   .limit(1)
                   .select("*")
                   .getItem(new ServerModel());
    }

    public static ServerModel getServerByIAAS(String iaas_key) throws SQLException {
        return db().table("water_ops_server")
                   .where("iaas_key = ?", iaas_key)
                   .limit(1)
                   .select("*")
                   .getItem(new ServerModel());
    }

    public static ConfigModel getServerIaasAccount(String iaas_key) throws SQLException {
        ServerModel sm = getServerByIAAS(iaas_key);
        if (sm == null || TextUtils.isEmpty(sm.iaas_account)) {
            return null;
        }

        ConfigModel cfg = DbWaterCfgApi.getConfigByTagName(sm.iaas_account);

        if (TextUtils.isEmpty(cfg.value)) {
            return null;
        } else {
            return cfg;
        }
    }

    public static void setServerAttr(String iaas_key, String iaas_attrs) throws SQLException{
        if(TextUtils.isEmpty(iaas_attrs)  || TextUtils.isEmpty(iaas_key)){
            return;
        }

        db().table("water_ops_server")
                .set("iaas_attrs",iaas_attrs)
                .whereEq("iaas_key",iaas_key)
                .update();
    }


    public static boolean updateServer(int server_id, String tag, String name, String address, String address_local, Integer iaas_type, String iaas_key, String iaas_account, String hosts_local, String note, int is_enabled, int env_type) throws SQLException {
        if (iaas_type == null) {
            iaas_type = 0;
        }

        DbTableQuery db = db().table("water_ops_server")
                              .set("tag", tag)
                              .set("name", name)
                              .set("address", address)
                              .set("iaas_type", iaas_type)
                              .set("iaas_key", iaas_key)
                              .set("iaas_account", iaas_account)
                              .set("address_local", address_local)
                              .set("hosts_local", hosts_local)
                              .set("note", note)
                              .set("is_enabled", is_enabled)
                              .set("env_type", env_type);

        if (server_id > 0) {
            return db.where("server_id = ?", server_id).update() > 0;
        } else {
            return db.insert() > 0;
        }

    }

//    public static List<ServerModel> getServerByType(List<Integer> types) throws SQLException {
//        return db().table("water_ops_server")
//                .where("iaas_type in (?...)", types).and("is_enabled = ?", 1)
//                .select("*")
//                .getList(new ServerModel());
//    }

    public static List<ServerTrackEcsModel> getServerEcsTracks(String tagAndName, String name, String sort) throws Exception {
        return db().table("water_ops_server s")
                   .leftJoin("water_ops_server_track_ecs t").on("s.iaas_type=0 AND s.is_enabled=1 AND s.iaas_key = t.iaas_key")
                   .where("s.iaas_type=0 AND s.is_enabled=1")
                   .and("s.iaas_account = ?", tagAndName)
                   .build((tb) -> {
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
                   .select("s.server_id,s.name,s.tag,s.iaas_type,s.iaas_attrs,s.sev_num,s.address_local,t.*,s.iaas_key")
                   .getList(new ServerTrackEcsModel());
    }


    public static void setServerEcsTracks(List<EcsTrackModel> list) throws Exception {
        for (EcsTrackModel m : list) {
            db().table("water_ops_server_track_ecs").usingExpr(true).log(false)
                .set("iaas_key", m.instanceId)
                .set("cpu_usage", m.cpu)
                .set("memory_usage", m.memory)
                .set("disk_usage", m.disk)
                .set("broadband_usage", m.broadband)
                .set("tcp_num", m.tcp)
                .set("last_updatetime", "$NOW()")
                .upsertBy("iaas_key");
        }
    }

    public static void setServerBlsTracks(List<BlsTrackModel> list) throws Exception {
        for (BlsTrackModel m : list) {
            db().table("water_ops_server_track_bls").usingExpr(true).log(false)
                .set("iaas_key", m.instanceId)
                .set("co_conect_num", m.co_conect_num)
                .set("new_conect_num", m.new_conect_num)
                .set("qps", m.qps)
                .set("traffic_tx", m.traffic_tx)
                .set("last_updatetime", "$NOW()")
                .upsertBy("iaas_key");
        }
    }

    public static void setServerDbsTracks(List<DbsTrackModel> list) throws Exception {
        for (DbsTrackModel m : list) {
            db().table("water_ops_server_track_dbs").usingExpr(true).log(false)
                .set("iaas_key", m.instanceId)
                .set("connect_usage", m.connect_usage)
                .set("cpu_usage", m.cpu_usage)
                .set("memory_usage", m.memory_usage)
                .set("disk_usage", m.disk_usage)
                .set("last_updatetime", "$NOW()")
                .upsertBy("iaas_key");
        }
    }

    public static List<ServerTrackBlsModel> getServerBlsTracks(String tagAndName, String name, String sort) throws Exception {
        return db().table("water_ops_server s")
                   .leftJoin("water_ops_server_track_bls t").on("s.iaas_type=1 AND s.is_enabled=1 AND s.iaas_key = t.iaas_key")
                   .where("s.iaas_type=1 AND s.is_enabled=1")
                   .and("s.iaas_account = ?", tagAndName)
                   .build((tb) -> {

                       if (TextUtils.isEmpty(name) == false) {
                           tb.and("s.name like ?", "%" + name + "%");
                       }

                       if (TextUtils.isEmpty(sort) == false) {
                           tb.orderBy("t." + sort + " DESC");
                       } else {
                           tb.orderBy("s.tag ASC,s.name ASC");
                       }
                   })
                   .select("s.server_id,s.name,s.tag,s.iaas_type,s.iaas_attrs,t.*,s.iaas_key")
                   .getList(new ServerTrackBlsModel());
    }

    public static List<ServerTrackDbsModel> getServerDbsTracks(String tagAndName, String name, String sort) throws Exception {
        return db().table("water_ops_server s")
                   .leftJoin("water_ops_server_track_dbs t").on("s.iaas_type>=2 AND s.is_enabled=1 AND s.iaas_key = t.iaas_key")
                   .where("s.iaas_type>=2 AND s.is_enabled=1")
                   .and("s.iaas_account = ?",  tagAndName)
                   .build((tb) -> {
                       if (TextUtils.isEmpty(name) == false) {
                           tb.and("s.name like ?", "%" + name + "%");
                       }

                       if (TextUtils.isEmpty(sort) == false) {
                           tb.orderBy("t." + sort + " DESC");
                       } else {
                           tb.orderBy("s.tag ASC,s.name ASC");
                       }
                   })
                   .select("s.server_id,s.name,s.tag,s.iaas_type,s.iaas_attrs,t.*,s.iaas_key")
                   .getList(new ServerTrackDbsModel());
    }

    ////////////////////////////////////////////////////

    //获取性能监控服务标签
    public static List<ServiceSpeedModel> getSpeedServices() throws SQLException {
        return db().table("water_reg_service_speed")
                   .groupBy("service")
                   .select("service,count(*) counts")
                   .getList(new ServiceSpeedModel());
    }

    public static List<ServiceSpeedModel> getServiceSpeedByService(String service) throws SQLException {
        return db().table("water_reg_service_speed")
                   .where("service = ?", service)
                   .select("*")
                   .getList(new ServiceSpeedModel());
    }

    public static List<ServiceSpeedModel> getServiceSpeedByService(String service, String tag) throws SQLException {
        return db().table("water_reg_service_speed")
                .where("service = ? AND tag=?", service,tag)
                .select("*")
                .getList(new ServiceSpeedModel());
    }

    //根据服务名和接口名获取性能监控列表
    public static List<ServiceSpeedModel> getSpeedsByServiceAndName(String service, String tag, String name, String sort) throws SQLException {
        if(tag==null && name!=null) {
            if (name.length() > 3 && name.endsWith("::")) {
                tag = name.substring(0, name.length() - 2);
                name = "";
            }
        }

        String tag1 = tag;
        String name1 =name;

        return db().table("water_reg_service_speed")
                   .where("service = ?", service)
                   .build(tb -> {
                       if (TextUtils.isEmpty(tag1) == false) {
                           tb.and("tag=?", tag1);
                       }

                       if (!TextUtils.isEmpty(name1)) {
                           tb.and("`name` like ?", "%" + name1 + "%");
                       }

                       if (TextUtils.isEmpty(sort)) {
                           tb.orderBy("tag ASC");
                       } else {
                           tb.orderBy(sort + " DESC");
                       }
                   })
                   .select("*")
                   .getList(new ServiceSpeedModel());
    }

    //接口的三天的请求频率
    public static JSONObject getSpeedForDate(String tag, String name_md5, String service, String field) throws SQLException {
        Datetime now = Datetime.Now();
        int date0 = now.getDate();
        int date1 = now.addDay(-1).getDate();
        int date2 = now.addDay(-1).getDate();


        JSONObject resp = new JSONObject();
        List<ServiceSpeedHourModel> threeDays = db().table("water_reg_service_speed_hour")
                                                    .where("tag = ?", tag)
                                                    .and("name_md5 = ?", name_md5)
                                                    .and("service = ?", service)
                                                    .and("log_date>=?", date2)
                                                    .orderBy("log_date DESC")
                                                    .select(field + " val,log_date,log_hour") //把字段as为val
                                                    .getList(new ServiceSpeedHourModel());

        Map<Integer, ServiceSpeedHourModel> list0 = new HashMap<>();
        Map<Integer, ServiceSpeedHourModel> list1 = new HashMap<>();
        Map<Integer, ServiceSpeedHourModel> list2 = new HashMap<>();
        for (ServiceSpeedHourModel m : threeDays) {
            if (m.log_date == date0) {
                list0.put(m.log_hour, m);
            }
            if (m.log_date == date1) {
                list1.put(m.log_hour, m);
            }
            if (m.log_date == date2) {
                list2.put(m.log_hour, m);
            }
        }

        Map<String, Map<Integer, ServiceSpeedHourModel>> data = new LinkedHashMap<>();
        data.put("today", list0);
        data.put("yesterday", list1);
        data.put("beforeday", list2);

        data.forEach((k, list) -> {
            JSONArray array = new JSONArray();
            for (int j = 0; j < 24; j++) {
                if (list.containsKey(j)) {
                    array.add(list.get(j).val);
                } else {
                    array.add(0);
                }
            }
            resp.put(k, array);
        });

        return resp;
    }


    //获取接口三十天响应速度情况
    public static JSONObject getSpeedForMonth(String tag, String name_md5, String service) throws SQLException {
        JSONObject resp = new JSONObject();

        List<ServiceSpeedDateModel> list = db().table("water_reg_service_speed_date")
                                               .whereEq("tag", tag)
                                               .andEq("name_md5", name_md5)
                                               .andEq("service", service)
                                               .orderBy("log_date DESC")
                                               .limit(30)
                                               .select("*")
                                               .getList(new ServiceSpeedDateModel());

        Collections.sort(list, (o1, o2) -> (o1.log_date - o2.log_date));

        JSONArray average = new JSONArray();
        JSONArray fastest = new JSONArray();
        JSONArray slowest = new JSONArray();
        JSONArray total_num = new JSONArray();
        JSONArray total_num_slow1 = new JSONArray();
        JSONArray total_num_slow2 = new JSONArray();
        JSONArray total_num_slow5 = new JSONArray();
        JSONArray dates = new JSONArray();
        for (ServiceSpeedDateModel m : list) {
            average.add(m.average);
            fastest.add(m.fastest);
            slowest.add(m.slowest);
            total_num.add(m.total_num);
            total_num_slow1.add(m.total_num_slow1);
            total_num_slow2.add(m.total_num_slow2);
            total_num_slow5.add(m.total_num_slow5);
            dates.add(m.log_date);
        }
        resp.put("average", average);
        resp.put("fastest", fastest);
        resp.put("slowest", slowest);
        resp.put("total_num", total_num);
        resp.put("total_num_slow1", total_num_slow1);
        resp.put("total_num_slow2", total_num_slow2);
        resp.put("total_num_slow5", total_num_slow5);
        resp.put("dates", dates);
        return resp;
    }



    public static List<ServerModel> getServers(int is_enabled) throws SQLException{
        return db().table("water_ops_server")
                .where("is_enabled = ?",is_enabled)
                .select("*")
                .getList(new ServerModel());
    }



}
