package webapp.controller.paas;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;
import org.noear.water.tools.ThrowableUtils;
import webapp.dao.db.DbPaaSApi;
import webapp.models.water_paas.PaasTmlModel;

import java.sql.SQLException;
import java.util.List;

@XController
@XMapping("/paas/tml")
public class TmlController extends BaseController {

    @XMapping("")
    public ModelAndView template(String tag, String tml_name) throws SQLException {

        List<String> tags = DbPaaSApi.tmlGetTags();

        viewModel.set("tags", tags);

        viewModel.set("tag", TextUtils.isNotEmpty(tag) ? tag : (tags.size() > 0 ? tags.get(0) : null));
        viewModel.set("tml_name", tml_name);

        return view("paas/tml");

    }

    @XMapping("inner")
    public ModelAndView inner(String tag, String tml_name, Integer _state) throws SQLException {

        if (null == _state) {
            _state = 0;
        }

        List<PaasTmlModel> list = DbPaaSApi.tmlGetList(tag, tml_name, _state ^ 1);

        viewModel.set("list", list);
        viewModel.set("tag", tag);
        viewModel.set("tml_name", tml_name);

        return view("paas/tml_inner");

    }

    @XMapping("edit")
    public ModelAndView edit(Integer tml_id) throws SQLException {

        if(tml_id == null || tml_id==0) {
            viewModel.set("tml", new PaasTmlModel());
        }else{
            viewModel.set("tml", DbPaaSApi.tmlGet(tml_id));
        }

        return view("paas/tml_edit");
    }

    @XMapping("ajax/save")
    public ViewModel save(int tml_id,
                          String tag,
                          String tml_name,
                          String name_display,
                          String code,
                          int is_enabled) {

        try {

            if (0 == tml_id) {
                DbPaaSApi.tmlAdd(tag, tml_name, name_display, code, is_enabled);
            } else {
                DbPaaSApi.tmlMol(tml_id, tag, tml_name, name_display, code, is_enabled);
            }

            viewModel.code(1, "成功");

        } catch (Exception e) {
            viewModel.code(0, ThrowableUtils.getString(e));
        }

        return viewModel;

    }

}
