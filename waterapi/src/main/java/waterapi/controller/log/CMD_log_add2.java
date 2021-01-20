package waterapi.controller.log;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.extend.validation.annotation.Whitelist;
import org.noear.water.WW;
import org.noear.water.log.LogEvent;
import org.noear.water.protocol.ProtocolHub;
import org.noear.water.utils.GzipUtils;
import waterapi.controller.UapiBase;
import waterapi.controller.UapiCodes;
import waterapi.dso.LogUtils;

import java.util.List;

/**
 * 批量添加日志（用于支持管道模式）
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Whitelist
@Controller
public class CMD_log_add2 extends UapiBase {

    @Mapping("/log/add2/")
    public Result cmd_exec(Context ctx) throws Exception {
        String contentType = ctx.contentType();
        String list_json = null;

        if (contentType != null) {
            if (contentType.startsWith(WW.mime_gzip)) {
                list_json = GzipUtils.unGZip(ctx.body());
            }

            if (contentType.startsWith(WW.mime_json)) {
                list_json = ctx.body();
            }
        }


        if (list_json == null) {
            list_json = ctx.param("list");
        }

        if (Utils.isEmpty(list_json)) {
            LogUtils.warn(ctx, contentType, "Context body or @list is null");
            throw UapiCodes.CODE_13("list");
        }

        List<LogEvent> list = ONode.deserialize(list_json);

        ProtocolHub.logStorer.writeAll(list);

        return Result.succeed();
    }
}
