package webapp.dao.db;

import org.noear.solon.extend.sessionstate.local.util.EncryptUtil;
import org.noear.water.admin.tools.dso.CacheUtil;
import org.noear.water.admin.tools.dso.Session;
import org.noear.water.client.WaterClient;
import org.noear.water.tools.Datetime;
import org.noear.water.tools.TextUtils;
import org.noear.weed.DataItem;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import webapp.Config;
import webapp.dao.IDUtil;
import webapp.models.TagCountsModel;
import webapp.models.water.*;
import webapp.models.water.LoggerModel;

import java.sql.SQLException;
import java.util.*;


public class DbWaterApi {
    private static DbContext db() {
        return Config.water;
    }

    public static List<LoggerModel> getLoggers(String tag) throws Exception {
        return db().table("water_base_logger")
                .where("is_enabled=1")
                .expre(tb -> {
                    if (!TextUtils.isEmpty(tag)) {
                        tb.and("tag = ?",tag);
                    }
                })
                .orderBy("logger asc")
                .select("*")
                .getList(LoggerModel.class);
    }

    //
    public static LoggerModel getLog(String logger) throws Exception {
        return db().table("water_base_logger")
                .where("logger = ?", logger)
                .limit(1)
                .select("*")
                .getItem(LoggerModel.class);
    }

    public static List<LoggerModel> getLoggerTag() throws Exception{
        return db().table("water_base_logger")
                .groupBy("tag")
                .select("tag")
                .getList(LoggerModel.class);
    }


    //加载IP白名单到静态缓存里
    public static void reloadWhitelist() throws Exception {
        CacheUtil.data.clear("whitelist_all");//waterapi那儿，过一分钟就会自动刷新
    }

    public static WhitelistModel getIPWhite(String ip) throws SQLException {
        return db().table("water_base_whitelist")
                .where("ip = ?", ip)
                .select("*").caching(CacheUtil.data)
                .cacheTag("whitelist_all")
                .getItem(new WhitelistModel());
    }

    //编辑更新config。
    public static boolean editcfg(Integer id, String tag, String key, Integer type, String url, String user, String password, String explain) throws SQLException {
        DbTableQuery db = db().table("water_base_config")
                .set("id", id)
                .set("tag", tag.trim())
                .set("key", key.trim())
                .set("type", type)
                .set("url", url.trim())
                .set("user", user.trim())
                .set("password", password.trim())
                .set("explain", explain.trim());
        if (id > 0) {
            boolean isOk = db.where("id = ?", id).update() > 0;

            WaterClient.Tool.updateConfig(tag,key);

            return isOk;
        } else {
            return db.insert() > 0;
        }
    }

    public static void addGateway(String tag, String key, String url, String explain) throws SQLException {
        db().table("water_base_config")
                .set("tag", tag.trim())
                .set("key", key.trim())
                .set("user", key.trim())
                .set("url", url.trim())
                .set("explain", explain.trim())
                .insert();
    }

    public static void modGateway(String tag, String ori_key, String key, String url, String explain) throws SQLException {
        db().table("water_base_config")
                .set("key", key.trim())
                .set("user", key.trim())
                .set("url", url.trim())
                .set("explain", explain.trim())
                .where("`tag` = ?", tag.trim())
                .and("`key` = ?", ori_key.trim())
                .update();
    }

    //获取标签数组。
    public static List<TagCountsModel> getTagGroup() throws SQLException {
        return db().table("water_base_config")
                .groupBy("tag")
                .select("tag,count(*) counts")
                .getList(TagCountsModel.class);
    }

    // 获取有特定类型配置的TAG
    public static List<TagCountsModel> getTagGroupWithType(int type) throws SQLException {
        return db().table("water_base_config")
                .where("type = ?", type)
                .groupBy("tag")
                .select("tag, COUNT(*) AS counts")
                .getList(TagCountsModel.class);
    }

    //编辑功能，根据id获取config信息。
    public static ConfigModel getConfigByRowId(Integer id) throws SQLException {
        return db().table("water_base_config")
                .where("id = ?", id)
                .select("*")
                .getItem(new ConfigModel());
    }


    //根据tag列出config。
    public static ConfigModel getConfigByTagName(String tagName) throws SQLException {
        if(TextUtils.isEmpty(tagName)){
            return new ConfigModel();
        }

        String[] ss = tagName.split("/");

        return getConfigByTagName(ss[0], ss[1]);
    }

    public static ConfigModel getConfigByTagName(String tag, String name) throws SQLException {
        return db().table("water_base_config")
                .where("tag = ?", tag)
                .and("`key` = ?", name)
                .limit(1)
                .select("*")
                .getItem(new ConfigModel());
    }

    public static List<ConfigModel> getConfigByTag(String tag,String key) throws SQLException {
        return db().table("water_base_config")
                .where("tag = ?", tag)
                .expre(tb -> {
                    if (!TextUtils.isEmpty(key)) {
                        tb.and("`key` like ?", "%" + key + "%");
                    }
                })
                .select("*")
                .getList(new ConfigModel());
    }

    public static List<ConfigModel> getGateways() throws SQLException {
        return db().table("water_base_config")
                .where("tag = ?", "_service")
                .select("*")
                .getList(new ConfigModel());
    }

    public static List<ConfigModel> getConfigByType(String tag,int type) throws SQLException {
        return db().table("water_base_config")
                .where("type = ?", type)
                .expre((tb)->{
                  if(TextUtils.isEmpty(tag) == false){
                      tb.and("tag = ?", tag);
                  }
                })
                .select("*")
                .getList(new ConfigModel());
    }

    //获取白名单表tag
    public static List<TagCountsModel> getWhiteListTags() throws Exception {
        return db().table("water_base_whitelist")
                .groupBy("tag")
                .select("tag,count(*) counts")
                .getList(TagCountsModel.class);
    }

    //获取ip白名单列表
    public static List<WhitelistModel> getWhiteList(String ip) throws SQLException {
        return db().table("water_base_whitelist")
                .where("1 = 1")
                .expre(tb -> {
                    if (TextUtils.isEmpty(ip) == false) {
                        tb.and("ip like ?", ip + "%");
                    }
                })
                .select("*")
                .getList(new WhitelistModel());
    }

    //新增ip白名单
    public static boolean addWhiteList(String tag, String ip, String note) throws SQLException {
        return db().table("water_base_whitelist")
                .set("tag", tag)
                .set("ip", ip)
                .set("note", note)
                .insert() > 0;
    }

    //新增ip白名单
    public static boolean deleteWhiteList(int id) throws SQLException {
        return db().table("water_base_whitelist")
                .where("id = ?", id)
                .delete() > 0;
    }

    public static List<EnumModel> getEnumListByType(String type) throws Exception {
        return db().table("water_base_enum")
                   .where("1 = 1")
                   .expre(tb -> {
                       if (TextUtils.isEmpty(type) == false) {
                           tb.andEq("type",type);
                       }
                   })
                   .orderBy("`type`, `value`")
                   .select("*")
                   .getList(EnumModel.class);
    }

    public static List<EnumModel> getEnumListOfCache(String type) throws Exception {
        return db().table("water_base_enum")
                .whereEq("type", type)
                .orderBy("`type`, `value`")
                .select("*")
                .caching(CacheUtil.data)
                .getList(EnumModel.class);
    }

    public static boolean updateEnum(Integer enum_id, String type, String name, Integer value) throws SQLException {

        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(name)) {
            return false;
        }

        DbTableQuery db = db().table("water_base_enum")
                              .set("name", name)
                              .set("type", type)
                              .set("value", value);
        if (enum_id > 0) {
            return db.where("id = ?", enum_id).update() > 0;
        } else {
            return db.insert() > 0;
        }
    }

    public static EnumModel getEnumById(int id) throws Exception {
        return db().table("water_base_enum")
                   .where("id = ?", id)
                   .select("*")
                   .getItem(EnumModel.class);
    }

    //====================================================

    //获取type=10的配置（结构化数据库）
    public static List<ConfigModel> getDbConfigs() throws SQLException {
        return db().table("water_base_config")
                .where("type = 10")
                .orderBy("`tag`,`key`")
                .select("*")
                .getList(new ConfigModel());
    }

    //获取type=10,11,12的配置（结构化数据库 + 非结构化数据库）
    public static List<ConfigModel> getDbConfigsEx() throws SQLException {
        return db().table("water_base_config")
                .where("type >=10 AND type<20")
                .select("*")
                .getList(new ConfigModel());
    }




    //获取logger表tag
    public static List<TagCountsModel> getLoggerTags() throws Exception {
        return db().table("water_base_logger")
                .groupBy("tag")
                .select("tag,count(*) counts")
                .getList(TagCountsModel.class);
    }

    //根据tag获取列表。
    public static List<LoggerModel> getLoggersByTag(String tag,int is_enabled, String sort) throws Exception {
        return db().table("water_base_logger")
                .where("tag = ?", tag)
                .and("is_enabled = ?",is_enabled)
                .expre((tb)->{
                    if(TextUtils.isEmpty(sort) == false){
                        tb.orderBy(sort+" DESC");
                    }else{
                        tb.orderBy("logger ASC");
                    }
                })
                .select("*")
                .getList(LoggerModel.class);

    }

    //根据id获取logger。
    public static LoggerModel getLoggerById(Integer logger_id) throws Exception {
        return db().table("water_base_logger")
                .where("id=?", logger_id)
                .limit(1)
                .select("*")
                .getItem(LoggerModel.class);
    }

    //保存logger。
    public static boolean updateLogger(Integer logger_id, String tag, String logger, String source, String note,int keep_days) throws SQLException {
        DbTableQuery db = db().table("water_base_logger")
                .set("tag", tag)
                .set("logger", logger)
                .set("keep_days", keep_days)
                .set("source", source)
                .set("note", note);
        if (logger_id > 0) {
            return db.where("id = ?", logger_id).update() > 0;
        } else {
            db.set("is_enabled",1);
            return db.insert() > 0;
        }
    }

    //删除logger
    public static boolean isEnableLogger(int logger_id,int is_enabled) throws SQLException{
        return db().table("water_base_logger")
                .where("id = ?",logger_id)
                .set("is_enabled",is_enabled)
                .update() > 0;
    }

    /** 备份表的数据版本 */
    public static void logVersion(String table,String keyName,Object keyValue0){

        if(TextUtils.isEmpty(keyName)  || keyValue0 == null){
            return;
        }

        String keyValue = keyValue0.toString();


        try {
            DataItem data = db().table(table).where(keyName + "=?", keyValue).limit(1).select("*").getDataItem();

            if (data == null || data.count() == 0) {
                return;
            }

            Datetime now_time = Datetime.Now();
            String data_json = data.toJson();
            String data_md5 =  EncryptUtil.md5(data_json);

            String old_data_md5 = getLastVersionMd5(table,keyName,keyValue);

            //如果md5一样，说明没什么变化
            if(data_md5.equals(old_data_md5)){
                return;
            }

            db().table("water_base_versions")
                    .set("table", table)
                    .set("key_name", keyName)
                    .set("key_value", keyValue)
                    .set("data", data_json)
                    .set("data_md5",data_md5)
                    .set("log_user", Session.current().getUserName())
                    .set("log_ip", Session.getIP())
                    .set("log_date", now_time.getDate())
                    .set("log_fulltime",now_time.getFulltime())
                    .insert();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /** 获取历史版本最近10个 */
    public static List<VersionModel> getVersions(String table,String keyName,String keyValue) throws SQLException{

        if(TextUtils.isEmpty(keyValue)){
            return new ArrayList<>();
        }


        return db().table("water_base_versions")
                .where("`table` = ?",table)
                .and("`key_name`=?",keyName)
                .and("`key_value`=?",keyValue)
                .orderBy("id DESC")
                .limit(10)
                .select("*")
                .getList(new VersionModel());
    }

    public static VersionModel getVersionByCommit(int commit_id) throws SQLException{

        return db().table("water_base_versions")
                .where("`id` = ?",commit_id)
                .limit(1)
                .select("*")
                .caching(CacheUtil.data)
                .getItem(new VersionModel());
    }

    /** 最后一个历史版本的MD5 */
    public static String getLastVersionMd5(String table,String keyName,String keyValue) throws SQLException{

        return db().table("water_base_versions")
                .where("`table` = ?",table)
                .and("`key_name`=?",keyName)
                .and("`key_value`=?",keyValue)
                .orderBy("commit_id DESC")
                .limit(1)
                .select("data_md5")
                .getValue("");
    }

    public static List<TagCountsModel> getReportTags() throws SQLException {
        return db().table("water_base_reportor")
                .groupBy("tag")
                .select("tag, count(*) counts")
                .getList(TagCountsModel.class);
    }

    public static List<ReportModel> getReportByTag(String tag) throws SQLException{
        return db().table("water_base_reportor")
                .where("tag = ?",tag)
                .select("*")
                .getList(ReportModel.class);
    }

    public static ReportModel getReportById(int id) throws SQLException{
        return db().table("water_base_reportor")
                .where("id = ?",id)
                .select("*")
                .getItem(ReportModel.class);
    }

    public static boolean setReport(int id,String tag,String name,String code,String note,String args) throws SQLException{
        DbTableQuery dq = db().table("water_base_reportor")
                .set("tag", tag)
                .set("name", name)
                .set("note", note)
                .set("args", args)
                .set("code", code);
        if (id>0){
            //update
            return dq.where("id = ?",id)
                    .update()>0;
        } else {
            //add
            return dq.set("create_fulltime",new Date())
                    .insert()>0;
        }
    }



    //
    // service
    //

    //删除服务。
    public static boolean delServiceById(int service_id) throws SQLException {

        ServiceModel m = getServiceById(service_id);

        boolean isOk = db().table("water_base_service")
                .where("id=?", service_id)
                .delete() > 0;

        //通知负载更新
        upstreamNotice(m.name);

        return isOk;
    }

    //修改服务启用禁用状态
    public static boolean disService(Integer service_id, Integer is_enabled) throws SQLException {
        ServiceModel m = getServiceById(service_id);

        boolean isOk = db().table("water_base_service")
                .where("id = ?", service_id)
                .set("is_enabled", is_enabled)
                .update() > 0;

        //通知负载更新
        upstreamNotice(m.name);

        return isOk;
    }


    //获取service表中的数据。
    public static List<ServiceModel> getServices(String name, boolean is_web, int is_enabled) throws SQLException {
        return db()
                .table("water_base_service")
                .where("is_enabled = ?", is_enabled)
                .expre(tb -> {
                    if(is_web){
                        tb.and("name LIKE ?","web:%");
                    }else{
                        tb.and("name NOT LIKE ?","web:%");
                    }

                    if (TextUtils.isEmpty(name) == false) {
                        if(name.startsWith("ip:")){
                            tb.and("address LIKE ?",name.substring(3)+"%");
                        }else{
                            tb.and("name like ?", name + "%");
                        }
                    }
                })
                .orderBy("name asc")
                .select("*")
                .getList(new ServiceModel());
    }

    public static ServiceModel getServiceById(int service_id) throws SQLException{
        return db()
                .table("water_base_service")
                .where("id = ?", service_id)
                .select("*")
                .getItem(new ServiceModel());
    }

    public static List<ServiceModel> getServicesByName(String name) throws SQLException {

        return db().table("water_base_service")
                .where("name = ?", name)
                .select("*")
                .getList(new ServiceModel());

    }

    public static boolean udpService(Integer service_id,String name,String address,String note,Integer check_type,String check_url) throws SQLException {
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(address)){
            return false;
        }

        if(service_id == null){
            service_id = 0;
        }

        DbTableQuery query = db()
                .table("water_base_service")
                .set("name", name)
                .set("address", address)
                .set("note",note)
                .set("check_type", check_type)
                .set("check_url", check_url);


        if (service_id == 0) {
            String key = IDUtil.buildGuid();
            query.set("check_last_time",new Date())
                    .set("key", key).insert();
        } else {
            query.where("id = ?", service_id).update();
        }

        return true;
    }


    public static List<ServiceConsumerModel> getServiceConsumers(String service) throws SQLException {

        return db().table("water_base_service_consumer")
                .where("service = ?", service)
                .orderBy("consumer asc")
                .select("*")
                .getList(ServiceConsumerModel.class);

    }



    //通知负载更新
    private static void upstreamNotice(String sev){
        if(sev.contains(":")){
            return;
        }

        WaterClient.Tool.updateCache("upstream:"+sev);
    }

}