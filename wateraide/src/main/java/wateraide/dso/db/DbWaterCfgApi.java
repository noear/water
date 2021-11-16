package wateraide.dso.db;

import org.noear.snack.ONode;
import org.noear.water.WW;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import wateraide.Config;
import wateraide.dso.ConfigType;
import wateraide.models.TagCountsModel;
import wateraide.models.water_cfg.BrokerModel;
import wateraide.models.water_cfg.ConfigModel;
import wateraide.models.water_cfg.LoggerModel;
import wateraide.models.water_cfg.WhitelistModel;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DbWaterCfgApi {

    private static DbContext db() {
        return Config.water;
    }

    public static List<LoggerModel> getLoggerByTag(String tag) throws Exception {
        return db().table("water_cfg_logger")
                .where("is_enabled=1")
                .build(tb -> {
                    if (!TextUtils.isEmpty(tag)) {
                        tb.and("tag = ?",tag);
                    }
                })
                .orderBy("logger asc")
                .select("*")
                .getList(LoggerModel.class);
    }

    //
    public static LoggerModel getLogger(String logger) {
        try {
            return db().table("water_cfg_logger")
                    .where("logger = ?", logger)
                    .limit(1)
                    .select("*")
                    .getItem(LoggerModel.class);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static List<LoggerModel> getLoggerList() {
        try {
            return db().table("water_cfg_logger")
                    .whereEq("is_enabled",1)
                    .selectList("*", LoggerModel.class);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    //
    public static BrokerModel getBroker(String broker) {
        try {
            return db().table("water_cfg_broker")
                    .where("broker = ?", broker)
                    .limit(1)
                    .select("*")
                    .getItem(BrokerModel.class);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static List<BrokerModel> getBrokerList() {
        try {
            return db().table("water_cfg_broker")
                    .whereEq("is_enabled",1)
                    .selectList("*", BrokerModel.class);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }


    //获取白名单表tag
    public static List<TagCountsModel> getWhitelistTags() throws SQLException {
        return db().table("water_cfg_whitelist")
                .groupBy("tag")
                .orderByAsc("tag")
                .select("tag,count(*) counts")
                .getList(TagCountsModel.class);
    }

    //获取ip白名单列表
    public static List<WhitelistModel> getWhitelistByTag(String tag_name, String key, int state) throws SQLException {
        return db().table("water_cfg_whitelist")
                .whereEq("is_enabled",state==1)
                .build(tb -> {
                    if(tag_name != null){
                        tb.andEq("tag",tag_name);
                    }

                    if (TextUtils.isEmpty(key) == false) {
                        tb.andLk("value", key + "%");
                    }
                })
                .select("*")
                .getList(WhitelistModel.class);
    }

    //新增ip白名单
    public static boolean setWhitelist(Integer row_id, String tag, String type, String value, String note) throws SQLException {
        if (row_id == null) {
            row_id = 0;
        }

        if(value == null){
            return false;
        }

        DbTableQuery qr = db().table("water_cfg_whitelist")
                .set("tag", tag.trim())
                .set("type", type.trim())
                .set("value", value.trim())
                .set("note", note);

        if (row_id > 0) {
            return qr.whereEq("row_id", row_id).update() > 0;
        } else {
            return qr.insert() > 0;
        }
    }

    //批量导入
    public static void impWhitelist(String tag, WhitelistModel wm) throws SQLException {
        if(TextUtils.isEmpty(tag) == false){
            wm.tag = tag;
        }

        if(TextUtils.isEmpty(wm.tag) || TextUtils.isEmpty(wm.value)){
            return;
        }

        db().table("water_cfg_whitelist")
                .set("tag", wm.tag)
                .set("type", wm.type)
                .set("value", wm.value)
                .set("note", wm.note)
                .insertBy("tag,type,value");
    }

    //删除
    public static boolean delWhitelist(int row_id) throws SQLException {
        return db().table("water_cfg_whitelist")
                .where("row_id = ?", row_id)
                .delete() > 0;
    }

    //批量删除
    public static void delWhitelistByIds(int act, String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(",")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        if(act == 9) {
            db().table("water_cfg_whitelist")
                    .whereIn("row_id", list)
                    .delete();
        }else {
            db().table("water_cfg_whitelist")
                    .set("is_enabled", (act == 1 ? 1 : 0))
                    .whereIn("row_id", list)
                    .update();
        }
    }

    public static WhitelistModel getWhitelist(int row_id) throws SQLException {
        return db().table("water_cfg_whitelist")
                .where("row_id = ?", row_id)
                .select("*")
                .getItem(WhitelistModel.class);
    }

    public static List<WhitelistModel> getWhitelistByIds(String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(","))
                                    .stream()
                                    .map(s->Integer.parseInt(s))
                                    .collect(Collectors.toList());

        return db().table("water_cfg_whitelist")
                .whereIn("row_id", list)
                .select("*")
                .getList(WhitelistModel.class);
    }



    public static void delConfigByIds(int act, String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(",")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());

        if (list.size() == 0) {
            return;
        }

        if (act == 9) {
            db().table("water_cfg_properties")
                    .whereIn("row_id", list)
                    .delete();
        } else {
            db().table("water_cfg_properties")
                    .set("is_enabled", (act == 1 ? 1 : 0))
                    .whereIn("row_id", list)
                    .update();
        }
    }


    //导入
    public static void impConfig(String tag, ConfigModel wm) throws SQLException {
        if(TextUtils.isEmpty(tag) == false){
            wm.tag = tag;
        }

        if(TextUtils.isEmpty(wm.tag) || TextUtils.isEmpty(wm.key) || TextUtils.isEmpty(wm.value)){
            return;
        }

        db().table("water_cfg_properties")
                .set("tag", wm.tag)
                .set("type", wm.type)
                .set("key", wm.key)
                .set("value", wm.value)
                .set("edit_mode", wm.edit_mode)
                .insertBy("tag,key");
    }

    public static List<ConfigModel> getConfigByIds(String ids) throws SQLException {
        List<Object> list = Arrays.asList(ids.split(","))
                .stream()
                .map(s->Integer.parseInt(s))
                .collect(Collectors.toList());

        return db().table("water_cfg_properties")
                .whereIn("row_id", list)
                .select("*")
                .getList(ConfigModel.class);
    }


    //编辑更新config。
    public static boolean setConfig(Integer row_id, String tag, String key, Integer type, String value, String edit_mode) throws SQLException {
        if(value == null){
            value = "";
        }

        value = WW.cfg_data_header + Base64Utils.encode(value.trim());

        DbTableQuery db = db().table("water_cfg_properties")
                .set("row_id", row_id)
                .set("tag", tag.trim())
                .set("key", key.trim())
                .set("type", type)
                .set("edit_mode", edit_mode)
                .set("update_fulltime", "$NOW()").usingExpr(true)
                .set("value", value);

        if (row_id > 0) {
            boolean isOk = db.where("row_id = ?", row_id).update() > 0;
            return isOk;
        } else {
            boolean isOk = db.insert() > 0;
            return isOk;
        }
    }

    public static void updConfig(String tag, String key, String value) throws SQLException {

        value = WW.cfg_data_header + Base64Utils.encode(value.trim());

        DbTableQuery tb = db().table("water_cfg_properties")
                .set("update_fulltime", "$NOW()").usingExpr(true)
                .set("value", value)
                .set("tag", tag)
                .set("key", key);


        tb.upsertBy("tag,key");
    }

    public static void delConfig(Integer row_id) throws SQLException {
        if (row_id == null) {
            return;
        }

        db().table("water_cfg_properties")
                .where("row_id=?", row_id)
                .delete();
    }


    //获取标签数组。
    public static List<TagCountsModel> getConfigTags() throws SQLException {
        return db().table("water_cfg_properties")
                .groupBy("tag")
                .orderByAsc("tag")
                .select("tag,count(*) counts")
                .getList(TagCountsModel.class);
    }

    // 获取有特定类型配置的TAG
    public static List<TagCountsModel> getConfigTagsByType(int type) throws SQLException {
        return db().table("water_cfg_properties")
                .where("type = ?", type)
                .groupBy("tag")
                .orderByAsc("tag")
                .select("tag, COUNT(*) AS counts")
                .getList(TagCountsModel.class);
    }

    //编辑功能，根据row_id获取config信息。
    public static ConfigModel getConfig(Integer row_id) throws SQLException {
        if(row_id == null || row_id == 0){
            return new ConfigModel();
        }

        return db().table("water_cfg_properties")
                .where("row_id = ?", row_id)
                .select("*")
                .getItem(ConfigModel.class);
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
        return getConfigByTagName(tag,name,false);
    }

    public static ConfigModel getConfigByTagName(String tag, String name, boolean cache) throws SQLException {
        return db().table("water_cfg_properties")
                .whereEq("tag", tag)
                .andEq("key", name)
                .limit(1)
                .select("*")
                .getItem(ConfigModel.class);
    }

    public static List<ConfigModel> getConfigsByTag(String tag, String key, int state) throws SQLException {
        return db().table("water_cfg_properties")
                .whereEq("tag", tag)
                .andEq("is_enabled", state == 1)
                .build(tb -> {
                    if (!TextUtils.isEmpty(key)) {
                        tb.and("`key` like ?", "%" + key + "%");
                    }
                })
                .selectList("*", ConfigModel.class);
    }

    public static List<ConfigModel> getConfigsByType(String tag, int type) throws SQLException {
        return db().table("water_cfg_properties")
                .where("type = ?", type)
                .build((tb) -> {
                    if (TextUtils.isEmpty(tag) == false) {
                        tb.and("tag = ?", tag);
                    }
                })
                .orderBy("key")
                .selectList("*", ConfigModel.class);
    }

    public static List<ConfigModel> getConfigTagKeyByType(String tag, int type) throws SQLException {
        return db().table("water_cfg_properties")
                .where("type = ?", type)
                .build((tb) -> {
                    if (TextUtils.isEmpty(tag) == false) {
                        tb.and("tag = ?", tag);
                    }
                })
                .selectList("tag,key", ConfigModel.class);
    }

    //====================================================

    //获取type=10的配置（结构化数据库）
    public static List<ConfigModel> getDbConfigs() throws SQLException {
        return db().table("water_cfg_properties")
                .whereEq("type", ConfigType.db)
                .orderBy("`tag`,`key`")
                .select("*")
                .getList(ConfigModel.class);
    }

    //获取type=10的配置（结构化数据库）
    public static List<ConfigModel> getLogStoreConfigs() throws SQLException {
        return db().table("water_cfg_properties")
                .whereEq("type", ConfigType.db)
                .orEq("type",ConfigType.water_logger)
                .orderBy("`tag`,`key`")
                .select("*")
                .getList(ConfigModel.class);
    }

    //获取type=10,11,12的配置（结构化数据库 + 非结构化数据库）
    public static List<ConfigModel> getDbConfigsEx() throws SQLException {
        return db().table("water_cfg_properties")
                .where("type >=10 AND type<20")
                .select("*")
                .getList(ConfigModel.class);
    }

    //====================================================

    //获取logger表tag
    public static List<TagCountsModel> getLoggerTags() throws Exception {
        return db().table("water_cfg_logger").whereEq("is_enabled",1)
                .groupBy("tag")
                .orderByAsc("tag")
                .select("tag,count(*) counts")
                .getList(TagCountsModel.class);
    }

    //根据tag获取列表。
    public static List<LoggerModel> getLoggersByTag(String tag_name, int is_enabled, String sort) throws Exception {
        return db().table("water_cfg_logger")
                .where("tag = ?", tag_name)
                .and("is_enabled = ?",is_enabled)
                .build((tb)->{
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
    public static LoggerModel getLogger(Integer logger_id) throws Exception {
        return db().table("water_cfg_logger")
                .where("logger_id=?", logger_id)
                .limit(1)
                .select("*")
                .getItem(LoggerModel.class);
    }

    //设置logger。
    public static boolean setLogger(Integer logger_id, String tag, String logger, String source, String note, int keep_days, int is_alarm) throws SQLException {
        DbTableQuery db = db().table("water_cfg_logger")
                .set("tag", tag)
                .set("logger", logger)
                .set("keep_days", keep_days)
                .set("source", source)
                .set("is_alarm", is_alarm)
                .set("note", note);
        if (logger_id > 0) {
            boolean isOk = db.where("logger_id = ?", logger_id).update() > 0;

            return isOk;
        } else {
            return db.set("is_enabled", 1).insert() > 0;
        }
    }

    //设置启用状态
    public static void setLoggerEnabled(int logger_id, int is_enabled) throws SQLException {
        db().table("water_cfg_logger")
                .where("logger_id = ?", logger_id)
                .set("is_enabled", is_enabled)
                .update();
    }

    public static void delLogger(Integer logger_id) throws SQLException {
        if(logger_id == null){
            return;
        }

        db().table("water_cfg_logger")
                .where("logger_id = ?", logger_id)
                .delete();
    }


    //获取 broker 表tag
    public static List<TagCountsModel> getBrokerTags() throws Exception {
        return db().table("water_cfg_broker").whereEq("is_enabled", 1)
                .groupBy("tag")
                .orderByAsc("tag")
                .selectList("tag,count(*) counts", TagCountsModel.class);
    }

    public static List<TagCountsModel> getBrokerNameTags() throws Exception {
        return db().table("water_cfg_broker").whereEq("is_enabled", 1)
                .groupBy("broker")
                .orderByAsc("broker")
                .selectList("broker tag,count(*) counts", TagCountsModel.class);
    }

    //根据tag获取列表。
    public static List<BrokerModel> getBrokersByTag(String tag_name, int is_enabled, String sort) throws Exception {
        return db().table("water_cfg_broker")
                .where("tag = ?", tag_name)
                .and("is_enabled = ?",is_enabled)
                .build((tb)->{
                    if(TextUtils.isEmpty(sort) == false){
                        tb.orderBy(sort+" DESC");
                    }else{
                        tb.orderBy("broker ASC");
                    }
                })
                .select("*")
                .getList(BrokerModel.class);

    }

    //根据id获取 broker。
    public static BrokerModel getBroker(Integer broker_id) throws Exception {
        return db().table("water_cfg_broker")
                .where("broker_id=?", broker_id)
                .limit(1)
                .select("*")
                .getItem(BrokerModel.class);
    }

    //设置 broker。
    public static boolean setBroker(Integer broker_id, String tag, String broker, String source, String note, int keep_days, int is_alarm) throws SQLException {
        DbTableQuery db = db().table("water_cfg_broker")
                .set("tag", tag)
                .set("broker", broker)
                .set("keep_days", keep_days)
                .set("source", source)
                .set("is_alarm", is_alarm)
                .set("note", note);
        if (broker_id > 0) {
            boolean isOk = db.where("broker_id = ?", broker_id).update() > 0;


            return isOk;
        } else {
            return db.set("is_enabled", 1).insert() > 0;
        }
    }

    //设置启用状态
    public static void setBrokerEnabled(int broker_id, int is_enabled) throws SQLException {
        db().table("water_cfg_broker")
                .where("broker_id = ?", broker_id)
                .set("is_enabled", is_enabled)
                .update();
    }

    public static void delBroker(Integer broker_id) throws SQLException {
        if(broker_id == null){
            return;
        }

        db().table("water_cfg_broker")
                .where("broker_id = ?", broker_id)
                .delete();
    }


    public static List<ConfigModel> getMsgStoreConfigs() throws SQLException {
        return db().table("water_cfg_properties")
                .whereEq("type", ConfigType.water_broker)
                .orderBy("`tag`,`key`")
                .select("*")
                .getList(ConfigModel.class);
    }

}
