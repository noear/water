package wateradmin.controller.ops;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import wateradmin.controller.BaseController;

import java.sql.SQLException;

@XController
@XMapping("/ops/")
public class DeployController extends BaseController {
    @XMapping("deploy")
    public ModelAndView deploy(String tag_name) throws SQLException {

        return view("ops/deploy");
    }
}
