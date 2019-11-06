package webapp.dao;

import org.noear.water.client.WaterClient;
import org.noear.water.client.model.ConfigM;
import org.noear.water.tools.RedisX;
import org.noear.water.tools.TextUtils;
import webapp.Config;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IDUtil {

    private static ReentrantLock lock = new ReentrantLock();
    private static RedisX _redis = null;

    public static long buildWindDeployNodeID() throws Exception{return getID("wind_deploy_node");}

    public static String buildGuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static String getCodeByID(long id) {
        long code = id;
        long key = 999999999;

        code = code + key;
        code = code - (key / 100);
        code = code + (key / 10000);
        code = code - (key / 1000000);
        code = code + 1;

        return Long.toString(code,36);
    }


    public static boolean isNumeric(String str){
        if(TextUtils.isEmpty(str)){
            return false;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static long getID(String tag) throws Exception {
        return buildID(tag, 1);
    }

    private static long buildID(String tag, long startIndex) throws Exception {
        tryInit();

        return _redis
                .open1(ru -> ru.key("ID").expire(60 * 60 * 24 * 365).hashIncr(tag, 1l))
                + startIndex;
    }

    private static void tryInit() {
        if (_redis == null) {
            lock.lock();

            try {
                ConfigM cfg = WaterClient.Config.get(Config.water_config_tag, "water_redis");
                _redis = cfg.getRd(1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            lock.unlock();
        }
    }
}
