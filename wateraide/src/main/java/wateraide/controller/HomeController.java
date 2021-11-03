package wateraide.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.WW;
import org.noear.water.utils.Base64Utils;
import wateraide.Config;
import wateraide.dso.InitUtils;


/**
 * @author noear 2021/10/31 created
 */
@Controller
public class HomeController extends BaseController {
    final String rdb_water_tml = "schema=water\nserver=localhost:3306\nusername=\npassword=";
    final String rdb_tml = "schema=\nserver=\nusername=\npassword=";

    @Mapping("/")
    public ModelAndView home(Context ctx) throws Exception {
        //1.开始连接
        if (Config.water == null) {
            String config = rdb_water_tml;
            String token = ctx.cookie("TOKEN");
            if (Utils.isNotEmpty(token)) {
                config = Base64Utils.decode(token);
            }

            viewModel.put("config", config);
            return view("setup_connect");
        }

        //2.开始初始化Water
        if (InitUtils.allowWaterInit(Config.water)) {
            return view("setup_init");
        }


        String step = Config.getCfg(WW.water, Config.water_setup_step).value;

        if (Utils.isNotEmpty(step)) {
            //初始化 Paas
            if ("1".equals(step)) {
                String cfg = Config.getCfg(WW.water, WW.water_redis).value;

                //初始化
                viewModel.put("config", cfg);
                return view("setup_init2");
            }

            if ("2".equals(step)) {
                String cfg = Config.getCfg(WW.water, WW.water_paas).value;
                if (Utils.isEmpty(cfg)) {
                    cfg = Config.getCfg(WW.water, WW.water).value;
                }

                viewModel.put("config", cfg);
                return view("setup_init3_paas");
            }

            if ("3".equals(step)) {
                String cfg = Config.getCfg(WW.water, WW.water_log_store).value;
                if (Utils.isEmpty(cfg)) {
                    cfg = Config.getCfg(WW.water, WW.water).value;
                }

                viewModel.put("config", cfg);
                return view("setup_init4_log");
            }

            if ("4".equals(step)) {
                String cfg = Config.getCfg(WW.water, WW.water_msg_store).value;
                if (Utils.isEmpty(cfg)) {
                    cfg = Config.getCfg(WW.water, WW.water).value;
                }

                viewModel.put("config", cfg);
                return view("setup_init5_msg");
            }
        }

        return view("setup");
    }
}
