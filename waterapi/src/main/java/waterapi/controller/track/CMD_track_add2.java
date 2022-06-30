package waterapi.controller.track;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.track.TrackEvent;
import org.noear.water.track.TrackEventGather;
import org.noear.water.track.TrackUtils;
import waterapi.Config;
import waterapi.controller.UapiBase;
import waterapi.controller.UapiCodes;
import waterapi.dso.LogUtils;

import java.util.Map;

/**
 * 批量添加日志（用于支持管道模式）
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Whitelist
@Controller
public class CMD_track_add2 extends UapiBase {

    @Mapping("/track/add2/")
    public Result cmd_exec(Context ctx) throws Exception {
        String data_json = ctx.param("data");

        if (Utils.isEmpty(data_json)) {
            LogUtils.warn(ctx, ctx.path(), "Context param @data is null");
            throw UapiCodes.CODE_13("data");
        }

        TrackEventGather gather = ONode.deserialize(data_json, TrackEventGather.class);

        if (gather != null) {
            Config.rd_track.open(ru -> {
                for (Map.Entry<String, TrackEvent> kv : gather.mainSet.entrySet()) {
                    TrackUtils.trackAll(ru, kv.getKey(), kv.getValue());
                }

                for (Map.Entry<String, TrackEvent> kv : gather.serviceSet.entrySet()) {
                    TrackUtils.trackAll(ru, kv.getKey(), kv.getValue());
                }

                for (Map.Entry<String, TrackEvent> kv : gather.fromSet.entrySet()) {
                    TrackUtils.trackAll(ru, kv.getKey(), kv.getValue());
                }
            });
        }

        return Result.succeed();
    }
}
