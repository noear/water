package waterapi.controller.register;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
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
@Whitelist
@XController
public class CMD_sev_track_api extends UapiBase {

    /**
     * @param service  服务名
     * @param tag      标签
     * @param name     名称
     * @param timespan 时长
     */
    @NotEmpty({"service", "tag", "name", "timespan"})
    @XMapping("/sev/track/")
    protected XResult cmd_exec(String _node, String _from,
                               String service, String tag, String name, long timespan) throws Exception {

        TrackUtils.track(Config.rd_track, service, tag, name, timespan, _node, _from);

        return XResult.succeed();
    }

    @NotEmpty({"service", "tag", "name", "timespan"})
    @XMapping("/sev/track/api/")
    protected XResult cmd_exec2(String _node, String _from,
                                String service, String tag, String name, long timespan) throws Exception {

        TrackUtils.track(Config.rd_track, service, tag, name, timespan, _node, _from);

        return XResult.succeed();
    }
}
