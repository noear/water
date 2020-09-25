package waterapi.controller.log;

import org.noear.snack.ONode;
import org.noear.solon.XUtil;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XResult;
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
 * 批量添加日志
 *
 * @author noear
 * @since 2017.07
 * Update time 2020.09
 */
@Whitelist
@XController
public class CMD_log_add2 extends UapiBase {

    @XMapping("/log/add2/")
    public XResult cmd_exec(XContext ctx) throws Exception {
        String contentType = ctx.contentType();
        String list_json = null;


        if (WW.mime_gzip.equals(contentType)) {
            list_json = GzipUtils.uncompressToString(ctx.bodyAsBytes());
        }

        if (WW.mime_json.equals(contentType)) {
            list_json = ctx.body();
        }

        if (list_json == null) {
            list_json = ctx.param("list");
        }

        if (XUtil.isEmpty(list_json) == false) {
            LogUtils.warn(ctx, contentType, "XContext body or @list is null");
            throw UapiCodes.CODE_13("list");
        }

        List<LogEvent> list = ONode.deserialize(list_json);

        ProtocolHub.logStorer.writeAll(list);

        return XResult.succeed();
    }
}
