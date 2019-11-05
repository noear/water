package webapp.dso.db;

import org.noear.water.tools.TextUtils;
import org.noear.weed.DbContext;
import webapp.Config;
import webapp.dso.CacheUtil;
import webapp.model.ConfigModel;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by yuety on 2017/7/18.
 */
public class DbApi {
    private static DbContext db(){
        return Config.water;
    }

    //获取配置项目
    public static DbContext getDbContext(String sourceKey, DbContext defDb) throws SQLException{

        if(sourceKey == null || sourceKey.indexOf(".")<0){
            return defDb;
        }else{
            String[] ss = sourceKey.split("\\.");
            ConfigModel cfg =  getConfig(ss[0],ss[1]);

            if(TextUtils.isEmpty(cfg.value)){
                return defDb;
            }else{
                return new DbContext(cfg.toProp());
            }
        }
    }

    public static List<ConfigModel> getConfigByTag(String tag) throws SQLException {
        return db().table("water_base_config")
                .whereEq("tag", tag)
                .select("*")
                .caching(CacheUtil.data)
                .getList(ConfigModel.class);
    }

    public static ConfigModel getConfig(String tag, String key) throws SQLException {
        return db().table("water_base_config")
                .where("tag=? AND `key`=?", tag, key)
                .select("*")
                .caching(CacheUtil.data)
                .getItem(ConfigModel.class);
    }

    public static ConfigModel getConfigNoCache(String tag, String key) throws SQLException {
        return db().table("water_base_config")
                .where("tag=? AND `key`=?", tag, key)
                .select("*")
                .getItem(ConfigModel.class);
    }

    //获取账号的手机号（用于报警）
    public static List<String> getAccountMobiles() throws SQLException{
        return Config.water.table("water_base_account")
                        .where("LENGTH(alarm_mobile) > 0")
                        .select("alarm_mobile")
                        .caching(CacheUtil.data)
                        .getArray("alarm_mobile");
    }

    //获取IP白名单
    private static List<String> _whitelist = null;
    private static synchronized List<String> getWhitelist() throws SQLException {
        if (_whitelist == null) {
            loadWhitelist();
        }

        return _whitelist;
    }

    //加载IP白名单到静态缓存里
    public static void loadWhitelist()throws SQLException{
        _whitelist = db().table("water_base_whitelist")
                         .select("ip")
                         .caching(CacheUtil.data)
                         .usingCache(60)
                         .getArray("ip");
    }

    //检查是否为IP白名单
    public static boolean isWhitelist(String ip) throws SQLException{
        return getWhitelist().contains(ip);
    }

    //检查是否为可访问的
    public static boolean isAccessible(String key) throws SQLException {
        if(TextUtils.isEmpty(key)){
            return false;
        }

        return db().table("water_base_account").where("access_key=?", key).exists();
    }

}
