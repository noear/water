package wateradmin.controller.ops;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;

import java.sql.SQLException;

@Controller
@Mapping("/ops/")
public class DeployController extends BaseController {
    @Mapping("deploy")
    public ModelAndView deploy(String tag_name) throws SQLException {

        return view("ops/deploy");
    }
}
