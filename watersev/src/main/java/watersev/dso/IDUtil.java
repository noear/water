package watersev.dso;

import org.noear.water.utils.Datetime;
import org.noear.water.utils.RedisX;
import watersev.Config;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by noear on 2017/6/21.
 */
public class IDUtil {
    public static String buildGuid(){
        return UUID.randomUUID().toString();
    }

    public static long buildLogID(){
        return getID("log_id");
    }

    public static long buildMsgID(){
        return getID("msg_id");
    }

    public static long getID(String tag) {
        if (Config.rd_ids == null) {
            return getByTime();
        } else {
            try {
                return Config.rd_ids.open1((ru) ->
                        ru.key("WATER_ID")
                                .expire(60 * 60 * 24 * 365)
                                .hashIncr(tag, 1l) + 1000000
                );

            } catch (Exception ex) {
                ex.printStackTrace();

                //不能记错，否则可能无限循环
                //LogUtil.error("IDUtil", null, "getID", ex);

                return getByTime();
            }
        }
    }


    private static long getByTime(){
        return System.currentTimeMillis() * 10000 + new Random(1000).nextInt();
    }

}
