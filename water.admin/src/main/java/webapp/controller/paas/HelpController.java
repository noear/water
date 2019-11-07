package webapp.controller.paas;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;

@XController
@XMapping("/paas/help")
public class HelpController extends BaseController {
    @XMapping("")
    public ModelAndView index(){
        return view("paas/help");
    }
}
