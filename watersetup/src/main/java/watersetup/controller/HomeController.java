package watersetup.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
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
    public ModelAndView home(String config) {
        if (Config.water == null) {
            if (Utils.isEmpty(config)) {
                config = rdb_water_tml;
            } else {
                Properties prop = Utils.buildProperties(config);
                if (prop.size() == 4) {
                    Config.tryInit(prop);
                }
            }
        }

        if (Config.water == null) {
            viewModel.put("config", config);

            return view("setup_init");
        } else {
            return view("setup");
        }
    }
}
