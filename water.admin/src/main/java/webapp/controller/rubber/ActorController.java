package webapp.controller.rubber;

import com.alibaba.fastjson.JSONObject;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;
import webapp.dao.BcfTagChecker;
import webapp.dao.db.DbRubberApi;
import webapp.models.water_rebber.ActorModel;
import webapp.models.water_rebber.ModelModel;

import java.sql.SQLException;
import java.util.List;


@XController
@XMapping("/rubber/")
public class ActorController extends BaseController {

    @XMapping("actor")
    public ModelAndView joiner(String tag,String name) throws SQLException {
        List<ModelModel> tags = DbRubberApi.getActorTags();

        BcfTagChecker.filter(tags, m -> m.tag);

        viewModel.put("tags",tags);
        if (TextUtils.isEmpty(tag) == false) {
            boolean result = DbRubberApi.containActorTag(tags, tag);

            if (result) {
                viewModel.put("tag", tag);
            } else {
                viewModel.put("tag", tags.get(0).tag);
            }

        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag", tags.get(0).tag);
            } else {
                viewModel.put("tag", null);
            }
        }
        viewModel.put("name",name);
        return view("rubber/actor");
    }


    //参与人员右侧列表
    @XMapping("actor/inner")
    public ModelAndView inner(String tag,String name) throws SQLException {
        List<ActorModel> models = DbRubberApi.getActorList(tag,name);
        viewModel.put("models",models);
        viewModel.put("tag", tag);
        viewModel.put("name",name);
        return view("rubber/actor_inner");
    }


    //参与人员编辑
    @XMapping("actor/edit")
    public ModelAndView edit(Integer actor_id,String tag) throws SQLException{
        viewModel.put("actor_id",actor_id);
        viewModel.put("tag",tag);

        return view("rubber/actor_edit");
    }

    @XMapping("actor/ajax/getactor")
    public ActorModel getActorModel(Integer actor_id) throws SQLException{
        if(actor_id==null){
            actor_id=0;
        }
        return DbRubberApi.getActorModel(actor_id);
    }


    //参与人员保存编辑
    @XMapping("actor/edit/ajax/save")
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
    @XMapping("actor/edit/ajax/del")
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
