package watersetup.controller;

import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Result;
import watersetup.Config;

import java.util.Properties;


/**
 * @author noear 2021/10/31 created
 */
@Controller
public class HomeController extends BaseController {
    final String rdb_water_tml = "schema=water\nserver=localhost:3306\nusername=\npassword=";
    final String rdb_tml = "schema=\nserver=\nusername=\npassword=";

    @Mapping("/")
    public ModelAndView home() {
        if (Config.water == null) {
            viewModel.put("config", rdb_water_tml);

            return view("setup_init");
        } else {
            return view("setup");
        }
    }

    @Mapping("/ajax/connect")
    public Result ajax_connect(String config) {
        if (Config.water == null) {
            Properties prop = Utils.buildProperties(config);
            if (prop.size() == 4) {
                if (Config.tryInit(prop) == false) {
                    return Result.failure("链接失败");
                }
            }
        }

        return Result.succeed();
    }
}
