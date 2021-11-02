package watersetup.controller;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.WW;
import watersetup.Config;
import watersetup.dso.InitUtils;


/**
 * @author noear 2021/10/31 created
 */
@Controller
public class HomeController extends BaseController {
    final String rdb_water_tml = "schema=water\nserver=localhost:3306\nusername=\npassword=";
    final String rdb_tml = "schema=\nserver=\nusername=\npassword=";

    @Mapping("/")
    public ModelAndView home() throws Exception {
        if (Config.water == null) {
            viewModel.put("config", rdb_water_tml);
            return view("setup_init");
        }

        if (InitUtils.allowWaterInit(Config.water)) {
            //还没有表或数据
            String water_cfg = Config.getCfg(WW.water, WW.water).value;

            if(Utils.isNotEmpty(water_cfg)){
                viewModel.put("config", rdb_water_tml);
            }else{
                viewModel.put("config", rdb_water_tml);
            }

            return view("setup_init");
        }


        String step = Config.getCfg(WW.water, Config.water_setup_step).value;

        if (Utils.isNotEmpty(step)) {
            String water_cfg = Config.getCfg(WW.water, WW.water).value;

            if ("1".equals(step)) {
                //初始化
                viewModel.put("config", water_cfg);
                return view("setup_init");
            }

            if ("2".equals(step)) {
                viewModel.put("config", water_cfg);
                return view("setup_init");
            }
        }


        return view("setup");
    }
}
