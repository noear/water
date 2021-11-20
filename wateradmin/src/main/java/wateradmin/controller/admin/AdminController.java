package wateradmin.controller.admin;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

/**
 * @author noear 2021/11/20 created
 */
@Mapping("/admin/")
@Controller
public class AdminController {
    @Mapping
    public String home(){
        return "{code:1}";
    }
}
