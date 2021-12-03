package xwater.controller.init;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.water.utils.Base64Utils;
import xwater.Config;

import java.util.Properties;

/**
 * @author noear 2021/11/2 created
 */
@Controller
public class ConnectController {

    @Post
    @Mapping("/ajax/connect")
    public Result ajax_connect(Context ctx, String config) throws Exception {
        if (Utils.isEmpty(config)) {
            return Result.failure("配置不能为空");
        }

        Properties props = Config.getProp(config);

        if (props.size() > 3) {
            Config.water = Config.getDb(props, true);

            if (Config.water != null) {
                //连接成功
                String token = Base64Utils.encode(config);
                ctx.cookieSet("WATERAIDE_TOKEN", token);
            }
        } else {
            return Result.failure("配置有问题...");
        }


        if (Config.water == null) {
            return Result.failure("连接失败");
        }

        //0.
        return Result.succeed(null, "连接成功");
    }
}
