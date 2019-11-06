package webapp.controller.paas;

import org.noear.water.utils.ExceptionUtil;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import webapp.controller.BaseController;
import webapp.dao.db.DbPaaSApi;
import webapp.models.water_paas.PaasTmlModel;
import webapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 2019.01.21
 *
 * @author cjl
 */
@XController
@XMapping("/paas/tml")
public class TmlController extends BaseController {

    @XMapping("")
    public ModelAndView template(String tag_name, String tml_name) throws SQLException {

        List<String> tags = DbPaaSApi.tmlGetTags();

        viewModel.set("tags", tags);

        viewModel.set("tag", StringUtils.isNotEmpty(tag_name) ? tag_name : (tags.size() > 0 ? tags.get(0) : null));
        viewModel.set("tml_name", tml_name);

        return view("paas/tml");

    }

    @XMapping("inner")
    public ModelAndView inner(String tag_name, String tml_name, Integer _state) throws SQLException {

        if (null == _state) {
            _state = 0;
        }

        List<PaasTmlModel> list = DbPaaSApi.tmlGetList(tag_name, tml_name, _state ^ 1);

        viewModel.set("list", list);
        viewModel.set("tag_name", tag_name);
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
            viewModel.code(0, ExceptionUtil.getFullStackTrace(e));
        }

        return viewModel;

    }

}
