package waterapi.dao.db;

import noear.weed.DataList;
import noear.weed.DbContext;
import org.apache.http.util.TextUtils;
import waterapi.Config;
import waterapi.dao.CacheUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by yuety on 2017/7/18.
 */
public class DbApi {
    private static DbContext db(){
        return Config.water;
    }
    private static DbContext dbr(){
        return Config.water_r;
    }

    private static List<String> _whitelist = null;
    private static synchronized List<String> getWhitelist() throws SQLException {
        if (_whitelist == null) {
            loadWhitelist();
        }

        return _whitelist;
    }

    public static void loadWhitelist()throws SQLException{
        _whitelist = db().table("whitelist")
                         .select("ip")
                         .caching(CacheUtil.data).usingCache(60*6)
                         .getArray("ip");
    }

    //是否为白名单
    public static boolean isWhitelist(String ip) throws SQLException{
        return getWhitelist().contains(ip);
    }

    //是否可访问
    public static boolean isAccess(String key) throws SQLException {
        if(TextUtils.isEmpty(key)){
            return false;
        }

        return dbr().table("access").where("access_key=?", key).exists();
    }

    ///////////////////////////////////////////////////
    //

    public static boolean hasSync(String key) throws SQLException{
        if(TextUtils.isEmpty(key)){
            return false;
        }

        return dbr().table("synchronous").where("`key`=?", key).exists();
    }

    public static boolean addSync(String key, int interval, String target,String target_pk, String source, String source_pk,int sync_type, String sync_fields) throws SQLException{

        return
        db().table("synchronous")
                .set("`key`",key)
                .set("`interval`",interval)
                .set("`target`",target)
                .set("`target_pk`",target_pk)
                .set("`source`",source)
                .set("`source_pk`",source_pk)
                .set("`sync_type`",sync_type)
                .set("`sync_fields`",sync_fields).insert() > 0;
    }

    public static DataList getSyncList() throws SQLException{
        return dbr().table("synchronous").select("*").getDataList();
    }
}
