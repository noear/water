package waterapi.dso.db;

import org.noear.water.WW;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;
import waterapi.Config;
import waterapi.dso.CacheUtils;
import waterapi.models.WhitelistModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author noear 2021/11/20 created
 */
public class DbWaterCfgSafeApi {
    private static DbContext db() {
        return Config.water;
    }

    //获取IP白名单
    private static List<String> _ip_whitelist = null;
    private static List<String> _token_whitelist = null;
    private static boolean _whitelist_ignore_client = false ;

    private static synchronized List<String> getIpWhitelist() throws SQLException {
        if (_ip_whitelist == null) {
            loadWhitelist();
        }

        return _ip_whitelist;
    }

    private static synchronized List<String> getTokenWhitelist() throws SQLException {
        if (_token_whitelist == null) {
            loadWhitelist();
        }

        return _token_whitelist;
    }

    public static boolean whitelistIgnoreClient(){
        return _whitelist_ignore_client;
    }

    //加载IP白名单到静态缓存里
    public static void loadWhitelist() throws SQLException {
        List<WhitelistModel> whiteList = db().table("water_cfg_whitelist")
                .whereEq("tag", "server")
                .andEq("is_enabled", 1)
                .caching(CacheUtils.data).usingCache(60)
                .selectList("value", WhitelistModel.class);

        List<String> ipList = new ArrayList<>();
        List<String> tokenList = new ArrayList<>();
        for (WhitelistModel w1 : whiteList) {
            if (WW.whitelist_type_ip.equals(w1.type)) {
                ipList.add(w1.value);
            }

            if (WW.whitelist_type_token.equals(w1.type)) {
                tokenList.add(w1.value);
            }
        }

        _ip_whitelist = ipList;
        _token_whitelist = tokenList;

        String tmp = DbWaterCfgApi.getConfig("water", "whitelist_ignore_client", 60).value;

        _whitelist_ignore_client = "1".equals(tmp);
    }

    /**
     * 检查是否为IP白名单
     * */
    public static boolean isWhitelistByIp(String ip) throws SQLException {
        if(TextUtils.isEmpty(ip)){
            return false;
        }

        return getIpWhitelist().contains(ip);
    }

    /**
     * 检查是否为Token白名单
     * */
    public static boolean isWhitelistByToken(String token) throws SQLException {
        if(TextUtils.isEmpty(token)){
            return false;
        }

        return getTokenWhitelist().contains(token);
    }

    public static boolean isWhitelist(String tags, String type, String value) throws SQLException {
        return db().table("water_cfg_whitelist")
                .whereEq("type", type)
                .andEq("value", value)
                .andEq("is_enabled",1)
                .build((tb)->{
                    if(tags.length()  > 0){
                        if(tags.indexOf(",") < 0){
                            tb.andEq("tag", tags);
                        }else{
                            tb.andIn("tag", Arrays.asList(tags.split(",")));
                        }

                    }
                })
                .caching(CacheUtils.data)
                .usingCache(60)
                .selectExists();

    }
}
