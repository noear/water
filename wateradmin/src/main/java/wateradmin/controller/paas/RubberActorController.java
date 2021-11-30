package wateradmin.controller.paas;


import com.alibaba.fastjson.JSONObject;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.water.utils.TextUtils;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import wateradmin.controller.BaseController;
import wateradmin.dso.BcfTagChecker;
import wateradmin.dso.SessionPerms;
import wateradmin.dso.db.DbRubberApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_paas.RebberActorModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;


@Controller
@Mapping("/rubber/")
public class RubberActorController extends BaseController {

    @Mapping("actor")
    public ModelAndView joiner(String tag_name,String name) throws SQLException {
        List<TagCountsModel> tags = DbRubberApi.getActorTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        viewModel.put("tags",tags);
        if (TextUtils.isEmpty(tag_name) == false) {
            boolean result = DbRubberApi.containActorTag(tags, tag_name);

            if (result) {
                viewModel.put("tag_name", tag_name);
            } else {
                viewModel.put("tag_name", tags.get(0).tag);
            }

        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag_name", tags.get(0).tag);
            } else {
                viewModel.put("tag_name", null);
            }
        }
        viewModel.put("name",name);
        return view("rubber/actor");
    }


    //参与人员右侧列表
    @Mapping("actor/inner")
    public ModelAndView inner(String tag_name,String name) throws SQLException {
        List<RebberActorModel> models = DbRubberApi.getActorList(tag_name,name);
        viewModel.put("models",models);
        viewModel.put("tag_name", tag_name);
        viewModel.put("name",name);
        return view("rubber/actor_inner");
    }


    //参与人员编辑
    @Mapping("actor/edit")
    public ModelAndView edit(Integer actor_id,String tag_name) throws SQLException{
        viewModel.put("actor_id",actor_id);
        viewModel.put("tag_name",tag_name);

        return view("rubber/actor_edit");
    }

    @Mapping("actor/ajax/getactor")
    public RebberActorModel getActorModel(Integer actor_id) throws SQLException{
        if(actor_id==null){
            actor_id=0;
        }
        return DbRubberApi.getActorModel(actor_id);
    }


    //参与人员保存编辑
    @AuthPermissions({SessionPerms.operator, SessionPerms.admin})
    @Mapping("actor/edit/ajax/save")
    public JSONObject editSave(Integer actor_id, String tag, String name, String name_display, String note) throws SQLException{
        JSONObject resp = new JSONObject();
        if(actor_id==null){
            actor_id=0;
        }
        boolean result = DbRubberApi.setActor(actor_id, tag, name, name_display, note);

        if (result) {
            resp.put("code",1);
            resp.put("msg","编辑成功");
        } else {
            resp.put("code",0);
            resp.put("msg","编辑失败");
        }

        return resp;
    }

    //参与人员删除
    @AuthPermissions({SessionPerms.operator, SessionPerms.admin})
    @Mapping("actor/edit/ajax/del")
    public ViewModel editDel(Integer actor_id) throws SQLException{
        boolean result = DbRubberApi.delActor(actor_id);

        if (result) {
            viewModel.code(1, "删除成功！");
        } else {
            viewModel.code(0, "删除失败!");
        }
        return viewModel;
    }


}
