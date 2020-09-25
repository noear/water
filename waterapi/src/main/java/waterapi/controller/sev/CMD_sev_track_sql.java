package waterapi.controller.sev;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XResult;
import org.noear.solon.extend.validation.annotation.NotEmpty;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.WW;
import waterapi.controller.UapiBase;
import waterapi.dso.db.DbWaterLogApi;

/**
 * Created by noear on 2017/7/19.
 */
@Whitelist
@XController
public class CMD_sev_track_sql extends UapiBase {

    @NotEmpty({"service", ""})
    @XMapping("/sev/track/sql/")
    public XResult cmd_exec(XContext ctx, String service, long interval,
                            String operator, String operator_ip, String path, String ua, String note) throws Exception {
        String schema = ctx.param("schema", "");
        String cmd_sql = ctx.param("cmd_sql", "");
        String cmd_arg = ctx.param("cmd_arg");

        String trace_id = ctx.header(WW.http_header_trace);

        DbWaterLogApi.addTrack(service, trace_id, schema, interval, cmd_sql, cmd_arg, operator, operator_ip, path, ua, note);

        return XResult.succeed();
    }
}
