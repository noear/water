package wateradmin.controller.admin;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;

/**
 * @author noear 2021/11/20 created
 */
@Controller
public class AdminController extends BaseController {
    @Mapping("/admin")
    public ModelAndView home(){
        return view("admin/setting.ftl");
    }
}
