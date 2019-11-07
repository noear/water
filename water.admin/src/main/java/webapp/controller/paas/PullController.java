package webapp.controller.paas;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;
import org.noear.water.tools.ThrowableUtils;
import webapp.dao.db.DbPaaSApi;
import webapp.models.water_paas.PaasPullModel;

import java.util.List;


@XController
@XMapping("/paas/pull")
public class PullController extends BaseController {

    @XMapping("")
    public ModelAndView template(String tag_name, String pull_name) throws Exception {

        List<String> tags = DbPaaSApi.pullGetTags();

        viewModel.set("tags", tags);

        viewModel.set("tag", TextUtils.isNotEmpty(tag_name) ? tag_name : (tags.size() > 0 ? tags.get(0) : null));
        viewModel.set("pull_name", pull_name);

        return view("paas/pull");

    }

    @XMapping("inner")
    public ModelAndView inner(String tag_name, String pull_name, Integer _state) throws Exception {

        if (null == _state) {
            _state = 0;
        }

        List<PaasPullModel> list = DbPaaSApi.pullGetList(tag_name, pull_name, _state ^ 1);

        viewModel.set("list", list);
        viewModel.set("tag_name", tag_name);
        viewModel.set("pull_name", pull_name);

        return view("paas/pull_inner");

    }

    @XMapping("edit")
    public ModelAndView edit(Integer pull_id) throws Exception {

        if(pull_id == null || pull_id==0) {
            viewModel.set("pull", new PaasPullModel());
        }else{
            viewModel.set("pull", DbPaaSApi.pullGet(pull_id));
        }

        return view("paas/pull_edit");
    }

    @XMapping("ajax/issue")
    public ViewModel issue(int pull_id) {
        try {
            DbPaaSApi.pullIssue(pull_id);

            viewModel.code(1, "成功");
        } catch (Exception e) {
            viewModel.code(0, ThrowableUtils.getString(e));
        }

        return viewModel;
    }

    @XMapping("ajax/save")
    public ViewModel save(int pull_id, String tag, String pull_name, String source, String target_dir,String target_url, int is_enabled) {
        try {
            DbPaaSApi.pullEdit(pull_id, tag, pull_name, source, target_dir,target_url, is_enabled);

            viewModel.code(1, "成功");

        } catch (Exception e) {
            viewModel.code(0, ThrowableUtils.getString(e));
        }

        return viewModel;
    }
}
