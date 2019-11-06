package webapp.controller.cfg;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.tools.TextUtils;
import webapp.controller.BaseController;
import webapp.dao.Session;
import webapp.dao.db.DbWaterApi;
import webapp.models.water.EnumModel;
import webapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;


@XController
@XMapping("/cfg/")
public class EnumController extends BaseController {

    //枚举列表
    @XMapping("enum")
    public ModelAndView enumList(String group) throws Exception {
        List<EnumModel> list = DbWaterApi.getEnumListByGroup(group);
        viewModel.put("list", list);
        return view("/cfg/enum");
    }

    //跳转枚举编辑页面
    @XMapping("enum/edit")
    public ModelAndView enumEdit(Integer enum_id) throws Exception {
        if (enum_id == null) {
            enum_id = 0;
        }
        EnumModel e = DbWaterApi.getEnumById(enum_id);
        viewModel.put("e", e);
        return view("/cfg/enum_edit");
    }

    //保存枚举编辑
    @XMapping("enum/edit/ajax/save")
    public ViewModel saveEnumEdit(Integer enum_id, String group, String name, Integer value) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {

            group = group.trim();
            name = name.trim();

            if (TextUtils.isEmpty(group) || TextUtils.isEmpty(name)) {
                viewModel.code(0, "值不能为空");
                return viewModel;
            }

            boolean result = DbWaterApi.updateEnum(enum_id, group, name, value);

            if (result) {
                viewModel.code(1, "保存成功！");
            } else {
                viewModel.code(0, "保存失败！");
            }
        } else {
            viewModel.code(0, "没有权限！");
        }

        return viewModel;
    }
}
