package org.noear.water.protocol.solution;


import org.noear.water.WW;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.RedisX;

import java.util.Random;
import java.util.UUID;

public class IDUtils {
    public static RedisX rd_ids = ProtocolHub.cfg(WW.water_redis).getRd(1);   //db:1

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
        if (rd_ids == null) {
            return getByTime();
        } else {
            try {
                return rd_ids.open1((ru) ->
                        ru.key(WW.WATER_ID)
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
        if (rd_ids == null) {
            return getByTime();
        } else {
            try {
                String date = Datetime.Now().toString("yyyyMMdd");

                return rd_ids.open1(ru->ru.key(WW.WATER_ID_DATE)
                        .expire(60 * 60 * 25)
                        .hashIncr(tag + "_" + date, 1l) + start);

            } catch (Exception ex) {
                ex.printStackTrace();

                return getByTime();
            }
        }
    }

    public static long getIDOfHour(String tag, long start) {
        if (rd_ids == null) {
            return getByTime();
        } else {
            try {
                String dh = Datetime.Now().toString("yyyyMMddHH");

                return rd_ids.open1(ru-> ru.key(WW.WATER_ID_HOUR)
                        .expire(60 * 60 * 2)
                        .hashIncr(tag + "_" + dh, 1l) + start);
            } catch (Exception ex) {
                ex.printStackTrace();

                return getByTime();
            }
        }
    }

    private static long getByTime(){
        return System.currentTimeMillis() * 10000 + new Random(1000).nextInt();
    }

}
