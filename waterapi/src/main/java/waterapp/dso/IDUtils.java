package waterapp.dso;


import org.noear.water.utils.Datetime;
import waterapp.Config;

import java.util.Random;
import java.util.UUID;

public class IDUtils {
    public static String buildGuid(){
        return UUID.randomUUID().toString();
    }

    public static long buildLogID(){
        return getID("log_id");
    }


    public static long buildLogID(String logger) {
        return getID(logger + "_log_id");
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

    public static long getIDOfDate(String tag, long start) {
        if (Config.rd_ids == null) {
            return getByTime();
        } else {
            try {
                String date = Datetime.Now().toString("yyyyMMdd");

                return Config.rd_ids.open1(ru->ru.key("WATER_ID_DATE")
                        .expire(60 * 60 * 25)
                        .hashIncr(tag + "_" + date, 1l) + start);

            } catch (Exception ex) {
                ex.printStackTrace();

                LogUtils.error("IDUtil",null,"getIDOfDate", ex);

                return getByTime();
            }
        }
    }

    public static long getIDOfHour(String tag, long start) {
        if (Config.rd_ids == null) {
            return getByTime();
        } else {
            try {
                String dh = Datetime.Now().toString("yyyyMMddHH");

                return Config.rd_ids.open1(ru-> ru.key("WATER_ID_HOUR")
                        .expire(60 * 60 * 2)
                        .hashIncr(tag + "_" + dh, 1l) + start);
            } catch (Exception ex) {
                ex.printStackTrace();

                LogUtils.error("IDUtil",null,"getIDOfHour", ex);

                return getByTime();
            }
        }
    }

    private static long getByTime(){
        return System.currentTimeMillis() * 10000 + new Random(1000).nextInt();
    }

}
