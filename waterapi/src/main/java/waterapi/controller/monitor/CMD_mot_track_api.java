package waterapi.controller.monitor;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.track.TrackUtils;
import waterapi.Config;
import waterapi.controller.UapiBase;

/**
 * 服务接口跟踪（此接口之后会弃用）
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Deprecated
@Whitelist
@Controller
public class CMD_mot_track_api extends UapiBase {

    /**
     * @param service  服务名
     * @param tag      标签
     * @param name     名称
     * @param timespan 时长
     */
    @Deprecated
    @NotEmpty({"service", "tag", "name", "timespan"})
    @Mapping("/mot/track/")
    protected Result cmd_exec(String _node, String _from,
                               String service, String tag, String name, long timespan) throws Exception {

        TrackUtils.track(Config.rd_track, service, tag, name, timespan, _node, _from);

        return Result.succeed();
    }

    @Deprecated
    @NotEmpty({"service", "tag", "name", "timespan"})
    @Mapping("/mot/track/api/")
    protected Result cmd_exec2(String _node, String _from,
                                String service, String tag, String name, long timespan) throws Exception {

        TrackUtils.track(Config.rd_track, service, tag, name, timespan, _node, _from);

        return Result.succeed();
    }
}
