package wateradmin.controller.admin;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

/**
 * @author noear 2021/11/20 created
 */
@Controller
public class AdminController {

    @Mapping("/admin")
    public String home(){
        return "{code:1}";
    }
}
