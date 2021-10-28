package waterapi.controller.run;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import org.noear.water.protocol.ProtocolHub;
import waterapi.controller.UapiBase;

/**
 * 缓存更新
 *
 * @author noear
 * @since 2021.10
 */
@Controller
public class CMD_run_cache_update extends UapiBase {
    @Mapping("/run/cache/update/")
    public Result cmd_exec(String tags) throws Exception {
        if (Utils.isNotEmpty(tags)) {
            for (String tag : tags.split(";")) {
                if (Utils.isEmpty(tag) == false) {
                    handlerDo(tag);
                }
            }
        }

        return Result.succeed();
    }

    private void handlerDo(String tag) throws Exception {
        if (tag.indexOf(":") > 0) {
            String[] ss = tag.split(":");

            if ("logger".equals(ss[0])) {
                if (ProtocolHub.logSourceFactory != null) {
                    ProtocolHub.logSourceFactory.updateSource(ss[1]); //尝试更新源
                }
                return;
            }
        }
    }
}