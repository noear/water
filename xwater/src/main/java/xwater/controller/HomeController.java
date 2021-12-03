package xwater.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.WW;
import xwater.Config;
import xwater.dso.Base64Utils2;
import xwater.dso.InitUtils;


/**
 * @author noear 2021/10/31 created
 */
@Controller
public class HomeController extends BaseController {
    final String rdb_water_tml = "schema=water\nserver=localhost:3306\nusername=\npassword=";
    final String rdb_tml = "schema=\nserver=\nusername=\npassword=";

    @Mapping("/")
    public ModelAndView home(Context ctx) throws Exception {
        //0.开始连接
        if (Config.water == null) {
            String config = rdb_water_tml;
            String token = ctx.cookie("WATERAIDE_TOKEN");
            if (Utils.isNotEmpty(token)) {
                config = Base64Utils2.decode(token);
            }

            if (Utils.isEmpty(config)) {
                config = rdb_water_tml;
            }

            viewModel.put("config", config);
            return view("setup_connect");
        }

        //1.开始初始化 water
        if (InitUtils.allowWaterInit(Config.water)) {
            return view("setup_init");
        }


        String step = Config.getCfg(WW.water, Config.water_setup_step).value;

        if (Utils.isNotEmpty(step)) {
            //2. 开始配置 reids
            if ("1".equals(step)) {
                String cfg = Config.getCfg(WW.water, WW.water_redis).value;

                //初始化
                viewModel.put("config", cfg);
                return view("setup_init2");
            }

            //3. 开始配置 water paas
            if ("2".equals(step)) {
                String cfg = Config.getCfg(WW.water, WW.water_paas).value;
                if (Utils.isEmpty(cfg)) {
                    cfg = Config.getCfg(WW.water, WW.water).value;
                }

                viewModel.put("config", cfg);
                return view("setup_init3_paas");
            }

            //4. 开始配置 water log store
            if ("3".equals(step)) {
                String cfg = Config.getCfg(WW.water, WW.water_log_store).value;
                if (Utils.isEmpty(cfg)) {
                    cfg = Config.getCfg(WW.water, WW.water).value;
                }

                viewModel.put("config", cfg);
                return view("setup_init4_log");
            }

            //5. 开始配置 water msg store
            if ("4".equals(step)) {
                String cfg = Config.getCfg(WW.water, WW.water_msg_store).value;
                if (Utils.isEmpty(cfg)) {
                    cfg = Config.getCfg(WW.water, WW.water).value;
                }

                viewModel.put("config", cfg);
                return view("setup_init5_msg");
            }
        }

        //开始进入管理界面
        return view("setup");
    }
}
