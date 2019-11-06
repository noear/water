package webapp.dso;


import org.noear.water.tools.Datetime;
import org.noear.water.tools.RedisX;
import webapp.Config;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yuety on 2017/6/21.
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

        tryInit();

        if (_redis == null) {
            return getByTime();
        } else {
            try {
                return _redis.open1((ru) ->
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

    public static long getIDOfDate(String tag, long start) {
        tryInit();
        if (_redis == null) {
            return getByTime();
        } else {
            try {
                String date = Datetime.Now().toString("yyyyMMdd");

                return _redis.open1(ru->ru.key("WATER_ID_DATE")
                        .expire(60 * 60 * 25)
                        .hashIncr(tag + "_" + date, 1l) + start);

            } catch (Exception ex) {
                ex.printStackTrace();

                LogUtil.error("IDUtil",null,"getIDOfDate", ex);

                return getByTime();
            }
        }
    }

    public static long getIDOfHour(String tag, long start) {
        tryInit();
        if (_redis == null) {
            return getByTime();
        } else {
            try {
                String dh = Datetime.Now().toString("yyyyMMddHH");

                return _redis.open1(ru-> ru.key("WATER_ID_HOUR")
                        .expire(60 * 60 * 2)
                        .hashIncr(tag + "_" + dh, 1l) + start);
            } catch (Exception ex) {
                ex.printStackTrace();

                LogUtil.error("IDUtil",null,"getIDOfHour", ex);

                return getByTime();
            }
        }
    }

    private static long getByTime(){
        return System.currentTimeMillis() * 10000 + new Random(1000).nextInt();
    }

    private static ReentrantLock lock = new ReentrantLock();
    private static RedisX _redis = null;
    private static void tryInit() {
        if (_redis == null) {
            lock.lock();

            if (_redis == null) {
                try {
                    _redis = Config.rd_ids;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            lock.unlock();
        }
    }
}
