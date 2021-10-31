package watersetup.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;

/**
 * @author noear 2021/10/31 created
 */
@Controller
public class HomeController extends BaseController {
    @Mapping("/")
    public ModelAndView home() {
        return view("setup");
    }
}
