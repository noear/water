package wateradmin.controller.paas;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import wateradmin.Config;
import wateradmin.controller.BaseController;

@Controller
@Mapping("/luffy/help")
public class LuffyHelpController extends BaseController {
    @Mapping("")
    public void index(Context ctx){
        ctx.redirect(Config.faas_uri() +"/water/paas/help/api/");
    }
}
