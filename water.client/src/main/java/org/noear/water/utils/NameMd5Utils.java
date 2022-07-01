package org.noear.water.utils;

import org.noear.redisx.RedisClient;
import org.noear.water.WaterSetting;
import org.noear.water.track.TrackNames;

/**
 * @author noear 2022/7/1 created
 */
public class NameMd5Utils {
    public static RedisClient rd_track_md5;

    static {
        rd_track_md5 = WaterSetting.redis_track_cfg().getRd(6);
        org.noear.water.track.TrackNames.singleton().bind(rd_track_md5);
    }

    public static String getNameMd5(String name) {
        return TrackNames.singleton().getNameMd5(name);
    }

    public static String getName(String nameMd5) {
        return TrackNames.singleton().getName(nameMd5);
    }
}
