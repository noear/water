package org.noear.water.protocol.solution;

import org.noear.water.model.ConfigM;
import org.noear.water.protocol.IdBuilder;
import org.noear.water.utils.RedisX;

import java.util.Random;

/**
 * @author noear 2021/2/5 created
 */
public class IdBuilderImp implements IdBuilder {
    RedisX redisX;

    public IdBuilderImp(ConfigM cfg) {
        this.redisX = cfg.getRd(1);
    }

    public long getId(String tag) {
        if (redisX == null) {
            return getByTime();
        } else {
            try {
                return redisX.open1((ru) ->
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

    private static long getByTime() {
        return System.currentTimeMillis() * 10000 + new Random(1000).nextInt();
    }

}
