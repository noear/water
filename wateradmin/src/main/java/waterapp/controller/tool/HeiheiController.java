package waterapp.controller.tool;

import org.noear.snack.ONode;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.WaterClient;
import waterapp.controller.BaseController;
import waterapp.viewModels.ViewModel;

/**
 * @Author:Fei.chu
 * @Description:嘿嘿
 */

@XController
@XMapping("/tool/")
public class HeiheiController extends BaseController{
    @XMapping("heihei")
    public ModelAndView index() {
        return view("tool/heihei");
    }

    //提交嘿嘿调试
    @XMapping("heihei/ajax/submit")
    public ViewModel submit(String msg, String target) throws Exception {

        String response = WaterClient.Notice.heihei(target, msg);
        ONode obj = ONode.load(response);

        return viewModel.code(obj.get("code").getInt(), obj.get("msg").getString());
    }
}
