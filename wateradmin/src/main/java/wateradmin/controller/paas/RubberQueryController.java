package wateradmin.controller.paas;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.db.DbRubberQueryApi;
import wateradmin.models.water.CodeQueryModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Controller
@Mapping("/rubber/")
public class RubberQueryController extends BaseController {
    //代码查询视图跳转
    @Mapping("query")
    public ModelAndView plan(Integer code_type,String code) throws SQLException {
        List<CodeQueryModel> list = new ArrayList<>();
        if (code_type != null && TextUtils.isEmpty(code) == false) {
            list = DbRubberQueryApi.codeQuery(code_type, code);
        }
        viewModel.put("list",list);
        return view("rubber/query");
    }

    //查看代码
    @Mapping("query/code")
    public ModelAndView code(Integer id,Integer code_type) throws SQLException{
        String code = DbRubberQueryApi.queryCode(code_type, id);
        code = code.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
        viewModel.put("code",code);
        return view("rubber/query_code");
    }
}
