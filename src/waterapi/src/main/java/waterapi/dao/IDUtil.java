package waterapi.dao;


import waterapi.utils.DateUtil;
import waterapi.utils.RedisX;

import java.util.Date;
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
                RedisX.RedisUsing ru = _redis.open();

                long id = ru.key("WATER_ID")
                        .expire(60 * 60 * 24 * 365)
                        .hashIncr(tag, 1l) + 1000000;

                ru.close();

                return id;

            } catch (Exception ex) {
                ex.printStackTrace();

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
                int date = DateUtil.getDate(new Date());

                RedisX.RedisUsing ru = _redis.open();

               long id =  ru.key("WATER_ID_DATE")
                        .expire(60 * 60 * 25)
                        .hashIncr(tag + "_" + date, 1l) + start;

               ru.close();

               return id;

            } catch (Exception ex) {
                ex.printStackTrace();

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
                String dh = DateUtil.format(new Date(), "yyyyMMddHH");

                RedisX.RedisUsing ru = _redis.open();

                long id =ru.key("WATER_ID_HOUR")
                        .expire(60 * 60 * 2)
                        .hashIncr(tag + "_" + dh, 1l) + start;

                ru.close();

                return id;

            } catch (Exception ex) {
                ex.printStackTrace();

                return getByTime();
            }
        }
    }

    private static long getByTime(){
        return System.currentTimeMillis() * 10000 + new Random(1000).nextInt();
    }

    private static ReentrantLock lock = new ReentrantLock();
    private static RedisX _redis = null;
    private static void tryInit(){
        if(_redis == null){
            lock.lock();

            try {
                WaterApi.ConfigModel cfg = WaterApi.Config.get("water_redis");
                _redis = new RedisX(cfg.url, cfg.user, cfg.password, 1);
            }catch (Exception ex){
                ex.printStackTrace();
            }

            lock.unlock();
        }
    }
}
