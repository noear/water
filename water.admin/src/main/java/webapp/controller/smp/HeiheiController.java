package webapp.controller.smp;

import org.noear.snack.ONode;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.client.WaterClient;

@XController
@XMapping("/smp/")
public class HeiheiController extends BaseController {
    @XMapping("heihei")
    public ModelAndView index() {
        return view("smp/heihei");
    }

    //提交嘿嘿调试
    @XMapping("heihei/ajax/submit")
    public ViewModel submit(String msg, String target) throws Exception {

        String response = WaterClient.Tool.pushHeihei(target,msg);
        ONode obj = ONode.load(response);

        return viewModel.code(obj.get("code").getInt(), obj.get("msg").getString());
    }
}
