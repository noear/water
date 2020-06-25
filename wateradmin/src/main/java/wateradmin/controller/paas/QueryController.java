package wateradmin.controller.paas;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.solon.core.XContext;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.db.DbPaaSApi;
import wateradmin.models.water_paas.PaasFileModel;
import wateradmin.models.water_paas.PaasFileType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@XController
@XMapping("/paas/query")
public class QueryController extends BaseController {
    @XMapping("")
    public ModelAndView list(XContext ctx) throws SQLException {
        String key = ctx.param("key", "");
        int act = ctx.paramAsInt("act",11);

        key = Base64Utils.decode(key);

        List<PaasFileModel> list = null;
        if(TextUtils.isEmpty(key)){
            list = new ArrayList<>();
        }else{
            list = DbPaaSApi.getFileList(null, PaasFileType.all, false, key, act);
        }


        viewModel.put("key", key);


        viewModel.put("mlist", list);

        return view("paas/query");
    }
}
