package webapp.dso.db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.util.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import webapp.Config;
import webapp.dao.CacheUtil;
import webapp.dao.IDUtil;
import webapp.dao.Session;
import webapp.models.aliyun.BlsTrackModel;
import webapp.models.aliyun.DbsTrackModel;
import webapp.models.aliyun.EcsTrackModel;
import webapp.models.water.*;
import webapp.models.water_wind.*;
import webapp.utils.Datetime;
import webapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class DbWindApi {

    private static DbContext db() {
        return Config.water;
    }

    //获取项目标签
    public static List<WindProjectModel> getProjectTags() throws SQLException {
        return db().table("wind_project")
                   .groupBy("tag")
                   .select("tag,count(*) counts")
                   .getList(new WindProjectModel());
    }

    public static List<WindProjectModel> getProjectByTagName(String tag, Integer is_enabled) throws SQLException {
        return db().table("wind_project")
                   .where("tag = ?", tag)
                   .and("is_enabled = ?", is_enabled)
                   .orderBy("`name` ASC")
                   .select("*")
                   .getList(new WindProjectModel());
    }

    public static boolean addProject(String tag, String name, String git_url, String git_user, String git_password, String git_ssh,
                                     String host_plan, String service_name, String port_plan, int type) throws SQLException {
        return db().table("wind_project")
                   .set("tag", tag)
                   .set("name", name)
                   .set("git_url", git_url)
                   .set("git_user", git_user)
                   .set("git_password", git_password)
                   .set("git_ssh", git_ssh)
                   .set("host_plan", host_plan)
                   .set("service_name", service_name)
                   .set("port_plan", port_plan)
                   .set("type", type)
                   .insert() > 0;
    }

    //根据id获取project信息。
    public static WindProjectModel getProjectByID(int project_id) throws SQLException {
        return db().table("wind_project")
                   .where("project_id = ?", project_id)
                   .select("*")
                   .getItem(new WindProjectModel());
    }

    //修改project信息。
    public static Long updateProject(Integer project_id, String tag, String name, String note, String git_url, int type, String developer) throws SQLException {
        DbTableQuery db = db().table("wind_project")
                              .set("tag", tag)
                              .set("name", name)
                              .set("note", note)
                              .set("git_url", git_url)
                              .set("type", type)
                              .set("developer", developer);

        if (project_id > 0) {
            if (db.where("project_id = ?", project_id).update() != 0) {
                return Long.valueOf(project_id);
            } else {
                return 0L;
            }
        } else {
            return db.insert();
        }
    }

    ////////////////////////////////////////////////////

    //根据标签名称和状态获取服务列表。
    public static List<WindServerModel> getServerByTagNameAndState(String tag, int is_enabled) throws SQLException {
        return db().table("wind_server")
                   .where("tag = ?", tag)
                   .and("is_enabled = ?", is_enabled)
                   .select("*")
                   .getList(new WindServerModel());
    }


    //获取服务标签
    public static List<WindServerModel> getServerTags() throws SQLException {
        return db().table("wind_server")
                   .groupBy("tag")
                   .select("tag,count(*) counts")
                   .getList(new WindServerModel());
    }

    //禁用/启用 服务
    public static boolean disableServer(int server_id, int is_enabled) throws SQLException {
        return db().table("wind_server")
                   .where("server_id = ?", server_id)
                   .set("is_enabled", is_enabled)
                   .update() > 0;
    }

    public static boolean addServer(String tag, String name, String ip, String hosts, String note, int is_enabled, int env_type) throws SQLException {
        return db().table("wind_server")
                   .set("tag", tag)
                   .set("name", name)
                   .set("ip", ip)
                   .set("hosts", hosts)
                   .set("is_enabled", is_enabled)
                   .set("env_type", env_type)
                   .insert() > 0;
    }

    public static List<ConfigModel> getIAASAccionts() throws SQLException {
        return DbWaterApi.getConfigByType(null, 1001);//1001=阿里云账号
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

    public static WindServerModel getServerByID(int server_id) throws SQLException {
        return db().table("wind_server")
                   .where("server_id = ?", server_id)
                   .limit(1)
                   .select("*")
                   .getItem(new WindServerModel());
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

        if (TextUtils.isEmpty(cfg.password)) {
            return null;
        } else {
            return cfg;
        }
    }


    public static boolean updateServer(int server_id, String tag, String name, String address, String address_local, Integer iaas_type, String iaas_key, String iaas_account, String hosts_local, String note, int is_enabled, int env_type) throws SQLException {
        if (iaas_type == null) {
            iaas_type = 0;
        }

        DbTableQuery db = db().table("wind_server")
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
//        return db().table("wind_server")
//                .where("iaas_type in (?...)", types).and("is_enabled = ?", 1)
//                .select("*")
//                .getList(new ServerModel());
//    }

    public static List<ServerTrackEcsModel> getServerEcsTracks(String tag_name, String name, String sort) throws Exception {
        return db().table("wind_server s")
                   .leftJoin("wind_server_track_ecs t").on("s.iaas_type=0 AND s.is_enabled=1 AND s.iaas_key = t.iaas_key")
                   .where("s.iaas_type=0 AND s.is_enabled=1")
                   .and("s.iaas_account = ?", "wind/" + tag_name)
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

    public static List<ServerTrackBlsModel> getServerBlsTracks(String tag_name, String name, String sort) throws Exception {
        return db().table("wind_server s")
                   .leftJoin("wind_server_track_bls t").on("s.iaas_type=1 AND s.is_enabled=1 AND s.iaas_key = t.iaas_key")
                   .where("s.iaas_type=1 AND s.is_enabled=1")
                   .and("s.iaas_account = ?", "wind/" + tag_name)
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

    public static List<ServerTrackDbsModel> getServerDbsTracks(String tag_name, String name, String sort) throws Exception {
        return db().table("wind_server s")
                   .leftJoin("wind_server_track_dbs t").on("s.iaas_type>=2 AND s.is_enabled=1 AND s.iaas_key = t.iaas_key")
                   .where("s.iaas_type>=2 AND s.is_enabled=1")
                   .and("s.iaas_account = ?", "wind/" + tag_name)
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

    ////////////////////////////////////////////////////

    //获取性能监控服务标签
    public static List<ServiceSpeedModel> getSpeedServices() throws SQLException {
        return db().table("service_speed")
                   .groupBy("service")
                   .select("service,count(*) counts")
                   .getList(new ServiceSpeedModel());
    }

    public static List<ServiceSpeedModel> getServiceSpeedByService(String service) throws SQLException {
        return db().table("service_speed")
                   .where("service = ?", service)
                   .select("*")
                   .getList(new ServiceSpeedModel());
    }

    public static List<ServiceSpeedModel> getServiceSpeedByService(String service, String tag) throws SQLException {
        return db().table("service_speed")
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

        return db().table("service_speed")
                   .where("service = ?", service)
                   .expre(tb -> {
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
    public static JSONObject getSpeedForDate(String tag, String name, String service, String field) throws SQLException {
        Datetime now = Datetime.Now();
        int date0 = now.getDate();
        int date1 = now.addDay(-1).getDate();
        int date2 = now.addDay(-1).getDate();


        JSONObject resp = new JSONObject();
        List<ServiceSpeedHourModel> threeDays = db().table("service_speed_hour")
                                                    .where("tag = ?", tag)
                                                    .and("name = ?", name)
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

    public static JSONObject getSpeedReqTate_bak(String tag, String name, String service, Integer type) throws SQLException {
        JSONObject resp = new JSONObject();
        List<ServiceSpeedHourModel> threeDays = db().table("service_speed_hour")
                                                    .where("tag = ?", tag)
                                                    .and("name = ?", name)
                                                    .and("service = ?", service)
                                                    .groupBy("log_date")
                                                    .orderBy("log_date desc")
                                                    .limit(3)
                                                    .select("log_date")
                                                    .getList(new ServiceSpeedHourModel());

        for (int i = 0; i < 3; i++) {
            String key = "today";
            if (i == 1) {
                key = "yesterday";
            } else if (i == 2) {
                key = "beforeYesterday";
            }

            ServiceSpeedHourModel m = threeDays.get(i);
            JSONArray array = new JSONArray();
            try {
                List<ServiceSpeedHourModel> list = db().table("service_speed_hour")
                                                       .where("tag = ?", tag)
                                                       .and("name = ?", name)
                                                       .and("service = ?", service)
                                                       .and("log_date = ?", m.log_date)
                                                       .select("total_num,log_hour")
                                                       .getList(new ServiceSpeedHourModel());

                Map<Integer, Long> counts = new HashMap<>();
                for (ServiceSpeedHourModel m1 : list) {
                    counts.put(m1.log_hour, m1.total_num);
                }
                for (int j = 0; j < 24; j++) {
                    if (counts.get(j) == null) {
                        array.add(0);
                    } else {
                        array.add(counts.get(j));
                    }
                }
                resp.put(key, array);
            } catch (Exception ex) {
                for (int j = 0; j < 24; j++) {
                    array.add(0);
                }
                resp.put(key, array);
            }
        }

        return resp;
    }

    //获取接口三十天响应速度情况
    public static JSONObject getSpeedForMonth(String tag, String name, String service) throws SQLException {
        JSONObject resp = new JSONObject();

        List<ServiceSpeedDateModel> list = db().table("service_speed_date")
                                               .where("tag = ?", tag)
                                               .and("name = ?", name)
                                               .and("service = ?", service)
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

    public static List<WindScriptModel> getScriptTags() throws SQLException {
        return db().table("wind_script")
                   .groupBy("tag")
                   .select("tag,count(*) counts")
                   .getList(new WindScriptModel());
    }

    public static List<WindScriptModel> getScriptByTagNameAndState(String tag, Integer is_enabled) throws SQLException {
        return db().table("wind_script")
                   .where("tag = ?", tag)
                   .and("is_enabled = ?", is_enabled)
                   .select("*")
                   .getList(new WindScriptModel());
    }

    public static WindScriptModel getScriptByName(String name) throws SQLException {
        return db().table("wind_script")
                   .where("`name` = ?", name)
                   .select("*")
                   .getItem(new WindScriptModel());
    }

    public static WindScriptModel getScriptByID(int script_id) throws SQLException {
        return db().table("wind_script")
                   .where("script_id = ?", script_id)
                   .limit(1)
                   .select("*")
                   .getItem(new WindScriptModel());
    }

    public static Long editScript(Long script_id, String tag, String name, Integer type, Integer env, Integer is_enabled, String code, String operator) throws SQLException {
        DbTableQuery tb = db().table("wind_script").usingExpr(false)
                              .set("code", code)
                              .set("tag", tag)
                              .set("name", name.trim())
                              .set("type", type)
                              .set("env", env)
                              .set("is_enabled", is_enabled);

        if (script_id > 0) {
            tb.set("modifier", operator)
              .set("update_fulltime", new Date());
            return Long.valueOf(tb.where("script_id = ?", script_id).update());
        } else {
            tb.set("creator", operator)
              .set("create_fulltime", new Date());
            return tb.insert();
        }
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 根据ID获取计算资源对应操作配置
     * @Date:13:48 2018/12/11
     */
    public static List<WindOperateModel> getOperateByServerId(Integer server_id) throws SQLException {
        return db().table("wind_operate o").leftJoin("wind_script s")
                   .on("o.script_id = s.script_id")
                   .where("server_id = ?", server_id)
                   .and("o.type = ?", 0)
                   .orderBy("o.rank ASC")
                   .select("o.*,s.name as script_name,s.tag as script_tag")
                   .getList(new WindOperateModel());
    }

    public static List<WindOperateModel> getOperateByServer(Integer server_id) throws SQLException {
        return db().table("wind_operate")
                .where("server_id = ?", server_id)
                .orderBy("rank ASC")
                .select("*")
                .getList(new WindOperateModel());
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 保存操作配置
     * @Date:13:18 2018/12/12
     */
    public static Long setOperate(Long operate_id, String name, Integer type, Integer sev_id, Integer script_id, Integer rank) throws SQLException {
        DbTableQuery db = db().table("wind_operate")
                              .set("name", name)
                              .set("script_id", script_id)
                              .set("rank", rank);
        if (operate_id != null && operate_id > 0) {
            db.set("modifier", Session.current().getUserName())
              .set("update_fulltime", new Date());
            return Long.valueOf(db.where("operate_id = ?", operate_id).update());
        } else {
            if (type == 0) {
                db.set("server_id", sev_id);
            } else {
                db.set("service_id", sev_id);
            }
            db.set("type", type)
              .set("creator", Session.current().getUserName())
              .set("create_fulltime", new Date());
            return db.insert();
        }
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 删除操作
     * @Date:16:20 2018/12/12
     */
    public static Boolean deleteOperate(Long operate_id) throws SQLException {
        return db().table("wind_operate")
                   .where("operate_id = ?", operate_id)
                   .delete() > 0;
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 根据operate_id获取操作
     * @Date:18:05 2018/12/12
     */
    public static WindOperateModel getOperateById(Integer operate_id) throws SQLException {
        return db().table("wind_operate")
                   .where("operate_id = ?", operate_id)
                   .select("*")
                   .getItem(new WindOperateModel());
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 根据脚本ID获取参数
     * @Date:16:22 2018/12/14
     */
    public static List<WindFormalParamModel> getFormalParam(Integer script_id) throws SQLException {
        return db().table("wind_formal_param")
                   .where("script_id = ?", script_id)
                   .orderBy("param_id ASC")
                   .select("*")
                   .getList(new WindFormalParamModel());

    }

    /**
     * @Author:Yunlong Feng
     * @Description: 保存脚本参数
     * @Date:10:47 2018/12/18
     */
    public static Boolean setFormalParam(Long script_id, int param_id, String param_name, String param_note) throws SQLException {
        return db().table("wind_formal_param")
                   .set("script_id", script_id)
                   .set("param_id", param_id)
                   .set("param_name", param_name)
                   .set("param_note", param_note)
                   .insert() > 0;
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 清除之前旧的参数
     * @Date:11:01 2018/12/18
     */
    public static void deleteFormalParam(Long script_id) throws SQLException {
        db().table("wind_formal_param")
            .where("script_id = ?", script_id)
            .delete();
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 根据id获取实参
     * @Date:13:23 2018/12/18
     */
    public static List<WindActualParamModel> getActualParam(Integer operate_id, Integer script_id) throws SQLException {
        return db().table("wind_actual_param")
                   .where("operate_id = ?", operate_id)
                   .and("script_id = ?", script_id)
                   .select("*")
                   .getList(new WindActualParamModel());
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 保存操作的脚本参数值
     * @Date:15:55 2018/12/18
     */
    public static Boolean setActualParam(Long operate_id, Integer script_id, int param_id, String param_value) throws SQLException {
        return db().table("wind_actual_param")
                   .set("operate_id", operate_id)
                   .set("script_id", script_id)
                   .set("param_id", param_id)
                   .set("param_value", param_value)
                   .insert() > 0;
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 根据操作ID删除操作的参数
     * @Date:15:58 2018/12/18
     */
    public static Boolean deleteActualParam(Long operate_id) throws SQLException {
        return db().table("wind_actual_param")
                   .where("operate_id = ?", operate_id)
                   .delete() > 0;
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 保存脚本的参数概览
     * @Date:17:34 2018/12/18
     */
    public static void setScriptArgs(Long script_id, String scriptArgs) throws SQLException {
        db().table("wind_script")
            .set("args", scriptArgs)
            .where("script_id = ?", script_id)
            .update();
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 禁用项目
     * @Date:10:37 2018/12/19
     */
    public static boolean updateProjectStatus(Integer project_id, Integer is_enabled) throws SQLException {
        return db().table("wind_project")
                   .where("project_id = ?", project_id)
                   .set("is_enabled", is_enabled)
                   .update() > 0;
    }

    /**
     * @Author:Yunlong Feng
     * @Description:
     * @Date:13:45 2018/12/19
     */
    public static List<WindServerModel> getServerByEnv(Integer env_type) throws SQLException {
        return db().table("wind_server")
                   .where("env_type = ?", env_type)
                   .select("*")
                   .getList(new WindServerModel());
    }

    /**
     * @Author:Yunlong Feng
     * @Description:
     * @Date:14:02 2018/12/19
     */
    public static List<WindProjectResourceModel> getResourceById(int project_id) throws SQLException {
        return db().table("wind_project_resource")
                   .where("project_id = ?", project_id)
                   .select("*")
                   .getList(new WindProjectResourceModel());
    }

    /**
     * @Author:Yunlong Feng
     * @Description:
     * @Date:16:47 2018/12/24
     */
    public static void setProjectResource(Integer project_id, String production_host, String production_port, Integer server_id, String server_name, Integer env_type) throws SQLException {
        db().table("wind_project_resource")
            .set("project_id", project_id)
            .set("server_id", server_id)
            .set("domain", production_host)
            .set("port_plan", production_port)
            .set("server_name", server_name)
            .set("env_type", env_type)
            .insert();
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 根据ID删除项目资源设置
     * @Date:16:10 2018/12/25
     */
    public static Boolean deleteProject(Integer project_id) throws SQLException {
        return db().table("wind_project_resource")
                   .where("project_id = ?", project_id)
                   .delete() > 0;
    }

    /**
     * @Description: 根据脚本ID获取参数
     * @Date:16:22 2018/12/14
     */
    public static String getOperateParam(int operate_id, int script_id) throws SQLException {

        StringBuilder sb = new StringBuilder();

        db().table("wind_actual_param AS a")
            .leftJoin("wind_formal_param AS f")
            .on("a.script_id = f.script_id AND a.param_id = f.param_id")
            .where("a.operate_id = ?", operate_id)
            .and("a.script_id = ?", script_id)
            .orderBy("param_id ASC")
            .select("a.operate_id, a.script_id, a.param_id, f.param_name, a.param_value")
            .getList(new WindOperateParamModel())
            .forEach(op -> sb.append(op.param_name).append("=")
                             .append(op.param_value).append(";"));

        return sb.toString();
    }

    public static Map<String, String> getServiceDomains(List<String> names) throws Exception {

        Map<String, String> map = new HashMap<>();
        if (names.size() == 0) {
            return map;
        }

        db().table("wind_project")
            .where("name IN (?...)", names)
            .select("*")
            .getList(WindProjectModel.class)
            .forEach(d -> map.put(d.name, d.host_plan));

        return map;
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 根据任务状态获取任务列表
     * @Date:9:53 2018/12/26
     */
    public static List<WindDeployTaskModel> getDeployTask(Integer is_over) throws Exception {
        List<WindDeployTaskModel> tasks = db().table("wind_deploy_task")
                                             .where("is_over = ?", is_over)
                                             .select("*")
                                             .getList(new WindDeployTaskModel());

        List<Long> list = tasks.stream().map(d -> d.task_id).collect(Collectors.toList());

        Map<Long, String> map = new HashMap<>();
        Config.water.sql("SELECT * FROM wind_deploy_flow WHERE flow_id IN (SELECT MAX(flow_id) " +
                                 "FROM wind_deploy_flow WHERE task_id IN (?...) GROUP BY task_id)", list)
                    .getList(WindDeployFlowModel.class).forEach(w -> map.put(w.task_id, w.desc));

        tasks.forEach(w -> {
            String desc = map.get(w.task_id);
            if (desc != null) {
                w.desc = desc;
            }
        });

        return tasks;
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 获取启用中的项目
     * @Date:15:52 2018/12/26
     */
    public static List<WindProjectModel> getProjectList() throws SQLException {
        return db().table("wind_project")
                   .where("is_enabled = ?", 1)
                   .select("*")
                   .getList(new WindProjectModel());
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 保存任务
     * @Date:16:54 2018/12/26
     */
    public static boolean setDeployTask(Integer deploy_id, Integer project_id, String project_name,
                                        String developer, String product_manager, String version, String note) throws SQLException {
        return db().table("wind_deploy_task")
                   .set("deploy_id", deploy_id)
                   .set("project_id", project_id)
                   .set("project_name", project_name)
                   .set("developer", developer)
                   .set("product_manager", product_manager)
                   .set("version", version)
                   .set("note", note)
                   .insert() > 0;
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 根据ID获取taskLog
     * @Date:9:40 2018/12/27
     */
    public static List<WindDeployFlowModel> getDeployFlowByTaskId(long task_id) throws SQLException {
        return db().table("wind_deploy_flow")
                   .where("task_id = ?", task_id)
                   .orderBy("flow_id ASC")
                   .select("*")
                   .getList(new WindDeployFlowModel());
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 根据task_id获取deployTask
     * @Date:10:05 2018/12/27
     */
    public static WindDeployTaskModel getDeployTaskById(long task_id) throws SQLException {
        return db().table("wind_deploy_task")
                   .where("task_id = ?", task_id)
                   .limit(1)
                   .select("*")
                   .getItem(new WindDeployTaskModel());
    }


    //根据项目id获取部署流程列表
    public static List<WindDeployModel> getDeployList(int project_id) throws SQLException{
        return db().table("wind_deploy")
                .where("project_id = ?",project_id)
                .select("*")
                .getList(new WindDeployModel());
    }

    //根据deploy_id获取部署流程列表
    public static List<WindDeployNodeModel> getDeployNodesByDeployId(int deploy_id) throws SQLException{
        return db().table("wind_deploy_node")
                .where("deploy_id = ?",deploy_id)
                .select("*")
                .getList(new WindDeployNodeModel());
    }

    //获取部署流程
    public static WindDeployModel getDeploy(int deploy_id) throws SQLException{
        return db().table("wind_deploy")
                .where("deploy_id = ?",deploy_id)
                .select("*")
                .getItem(new WindDeployModel());
    }

    //设置项目部署流程
    public static boolean setProjectDeploy(int project_id,int deploy_id,String name) throws SQLException{
        DbTableQuery dq = db().table("wind_deploy")
                .set("project_id", project_id)
                .set("name", name);

        if (deploy_id>0){
            //update
            dq.where("deploy_id = ?",deploy_id)
                    .update();
        } else {
            dq.insert();
        }
        return true;
    }

    //根据node_key获取节点
    public static WindDeployNodeModel getDeployNodeByKey(String node_key) throws SQLException{
        return db().table("wind_deploy_node")
                .where("node_key = ?",node_key)
                .select("*")
                .getItem(new WindDeployNodeModel());
    }

    public static List<WindServerModel> getServers(int is_enabled) throws SQLException{
        return db().table("wind_server")
                .where("is_enabled = ?",is_enabled)
                .select("*")
                .getList(new WindServerModel());
    }

    //设置流程节点
    public static boolean setDeployNode(int deploy_id,String node_key,String name,int operate_id,int project_id,int node_type,String prve_key,String next_key,int next_node_id) throws Exception{
        WindDeployNodeModel node = getDeployNodeByKey(node_key);

        DbTableQuery dq = db().table("wind_deploy_node")
                .expre(tb->{
                    if (!TextUtils.isEmpty(prve_key)){
                        tb.set("prve_key",prve_key);
                    }
                    if (!TextUtils.isEmpty(next_key)){
                        tb.set("next_key",next_key);
                    }
                    if (next_node_id>0){
                        tb.set("next_node_id",next_node_id);
                    }
                })
                .set("note", name);

        if (node.id>0){
            //update
            dq.where("node_key = ?",node_key)
                    .update();
        } else {
            //add
            dq.set("node_key", node_key)
                    .set("project_id", project_id)
                    .set("deploy_id", deploy_id)
                    .set("node_id", IDUtil.buildWindDeployNodeID() + 115)
                    .set("node_type", node_type)
                    .set("operate_id", operate_id)
                    .set("node_key", node_key)
                    .insert();
        }
        return true;
    }

    public static ViewModel setDeployDesign(int project_id,int deploy_id, String detail) throws Exception {

        ViewModel viewModel = new ViewModel();

        List<WindDeployNodeModel> list = getDeployNodesByDeployId(deploy_id);
        List<String> orgList = new ArrayList<>();
        List<String> chartsList = new ArrayList<>();
        HashMap<String, Integer> orgMap = new HashMap<>();
        for (WindDeployNodeModel m:list) {
            orgList.add(m.node_key);
            orgMap.put(m.node_key,m.node_id);
        }

        JSONObject obj = JSONObject.parseObject(detail);
        JSONArray links = obj.getJSONArray("linkDataArray");
        JSONArray nodes = obj.getJSONArray("nodeDataArray");

        for (Object node:nodes) {
            JSONObject json = (JSONObject)node;
            String key = json.getString("key");
            String figure = json.getString("figure");
            chartsList.add(json.getString("key"));
            if (figure.equals("execute")||figure.equals("Diamond")||figure.equals("Circle")){
                Integer value = orgMap.get(key);
                if (value==null){
                    viewModel.put("msg","未设置节点内容");
                    return viewModel;
                }
            }
        }

        HashMap<String, String> nextMap = new HashMap<>();
        for (Object link:links) {
            JSONObject json = (JSONObject)link;
            String prve_key = json.getString("from");
            String next_key = json.getString("to");
            String key = prve_key+next_key;
            chartsList.add(key);
            Integer value = orgMap.get(key);
            nextMap.put(prve_key,key);
            if (value==null){
                //插入连接线节点
                Integer next_node_id = orgMap.get(next_key);
                setDeployNode(deploy_id,key,"连接线",0,project_id,-1,prve_key,next_key,next_node_id);
            }
        }

        List<WindDeployNodeModel> listUpdate = getDeployNodesByDeployId(deploy_id);
        HashMap<String, Integer> orgUpdateMap = new HashMap<>();
        listUpdate.stream().forEach(li->{orgUpdateMap.put(li.node_key,li.node_id);});

        nodes.stream().forEach(node->{
            boolean isEnd = false;
            JSONObject json = (JSONObject)node;
            String key = json.getString("key");
            System.out.println(json);
            try {
                int stepType = json.getInteger("stepType");
                if (stepType==4){
                    isEnd = true;
                }
            } catch (Exception ex){}

            Integer node_id = orgUpdateMap.get(key);
            String next_key = nextMap.get(key);
            Integer next_node_id = orgUpdateMap.get(next_key);
            try {
                if (!isEnd){
                    //非结束节点才有下个节点
                    updateDeployNodeNextId(node_id,next_node_id);
                }
            } catch (SQLException ex){
                ex.printStackTrace();
            }

        });

        //删除旧操作中多出的节点
        list.stream().forEach(lis -> {
            boolean contain = chartsList.contains(lis.node_key);
            if (!contain) {
                try {
                    deleteDeployNode(lis.node_key);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        db().table("wind_deploy")
                .set("design_detail",detail)
                .where("deploy_id = ?",deploy_id)
                .update();

        viewModel.put("msg","保存成功");
        return viewModel;
    }

    public static boolean deleteDeployNode(String node_key) throws SQLException{
        return db().table("wind_deploy_node")
                .where("node_key = ?",node_key)
                .delete()>0;
    }

    public static boolean updateDeployNodeNextId(int node_id,int next_node_id) throws SQLException{
        return db().table("wind_deploy_node")
                .set("next_node_id",next_node_id)
                .where("node_id = ?",node_id)
                .update()>0;
    }


}
