package wateradmin.controller.paas;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import wateradmin.Config;
import wateradmin.controller.BaseController;

@XController
@XMapping("/paas/help")
public class HelpController extends BaseController {
    @XMapping("")
    public void index(XContext ctx){
        ctx.redirect(Config.paas_uri() +"/water/paas/help/api/");
    }
}
