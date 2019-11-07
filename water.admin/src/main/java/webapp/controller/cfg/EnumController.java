package webapp.controller.cfg;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;
import org.noear.water.admin.tools.dso.Session;
import webapp.dao.BcfTagChecker;
import webapp.dao.db.DbWaterApi;
import webapp.models.TagCountsModel;
import webapp.models.water.EnumModel;

import java.sql.SQLException;
import java.util.List;


@XController
@XMapping("/cfg/enum")
public class EnumController extends BaseController {

    //枚举
    @XMapping("")
    public ModelAndView enum_(String tag) throws Exception {
        List<TagCountsModel> tags = DbWaterApi.getEnumTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        if (TextUtils.isEmpty(tag) == false) {
            viewModel.put("tag", tag);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag", tags.get(0).tag);
            } else {
                viewModel.put("tag", null);
            }
        }
        viewModel.put("tags", tags);
        return view("cfg/enum");
    }


    //枚举列表
    @XMapping("inner")
    public ModelAndView enumInner(String tag,String type) throws Exception {
        List<EnumModel> list = DbWaterApi.getEnumListByType(tag, type);
        viewModel.put("list", list);
        return view("/cfg/enum_inner");
    }

    //跳转枚举编辑页面
    @XMapping("edit")
    public ModelAndView enumEdit(Integer id) throws Exception {
        if (id == null) {
            id = 0;
        }
        EnumModel e = DbWaterApi.getEnumById(id);
        viewModel.put("e", e);
        return view("/cfg/enum_edit");
    }

    //保存枚举编辑
    @XMapping("edit/ajax/save")
    public ViewModel saveEnumEdit(Integer id, String tag, String type, String title, Integer value) throws SQLException {
        int is_admin = Session.current().getIsAdmin();
        if (is_admin == 1) {

            type = type.trim();
            title = title.trim();

            if (TextUtils.isEmpty(type) || TextUtils.isEmpty(title)) {
                viewModel.code(0, "值不能为空");
                return viewModel;
            }

            boolean result = DbWaterApi.updateEnum(id, tag, type, title, value);

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
