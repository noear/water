package webapp.controller.paas;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.tools.TextUtils;
import webapp.dao.db.DbPaaSQueryApi;
import webapp.models.water.CodeQueryModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@XController
@XMapping("/paas/")
public class QueryController extends BaseController {
    //代码查询视图跳转
    @XMapping("query")
    public ModelAndView plan(Integer code_type,String code) throws SQLException {
        List<CodeQueryModel> list = new ArrayList<>();
        if (code_type != null && TextUtils.isEmpty(code) == false) {
            list = DbPaaSQueryApi.codeQuery(code_type, code);
        }
        viewModel.put("list",list);
        return view("paas/query");
    }

    //查看代码
    @XMapping("query/code")
    public ModelAndView code(Integer id,Integer code_type) throws SQLException{
        String code = DbPaaSQueryApi.queryCode(code_type, id);
        code = code.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
        viewModel.put("code_type", code_type);
        viewModel.put("code",code);
        return view("paas/query_code");
    }
}
