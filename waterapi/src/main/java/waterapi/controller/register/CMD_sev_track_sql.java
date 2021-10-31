package waterapi.controller.register;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.water.WW;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterLogApi;

/**
 * 服务SQL跟踪
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Deprecated
@Whitelist
@Controller
public class CMD_sev_track_sql extends UapiBase {

    /**
     * @param service 服务名
     * @param interval 时长
     * */
    @Deprecated
    @NotEmpty({"service", "interval"})
    @Mapping("/sev/track/sql/")
    public Result cmd_exec(Context ctx, String service, long interval,
                            String operator, String operator_ip, String path, String ua, String note) throws Exception {
        String schema = ctx.param("schema", "");
        String cmd_sql = ctx.param("cmd_sql", "");
        String cmd_arg = ctx.param("cmd_arg");

        String trace_id = ctx.header(WW.http_header_trace);

        DbWaterLogApi.addTrack(service, trace_id, schema, interval, cmd_sql, cmd_arg, operator, operator_ip, path, ua, note);

        return Result.succeed();
    }
}
