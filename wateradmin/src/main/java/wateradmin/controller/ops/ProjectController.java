package wateradmin.controller.ops;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.water.utils.TextUtils;
import wateradmin.controller.BaseController;
import wateradmin.dso.Session;
import wateradmin.dso.db.DbWaterProjectApi;
import wateradmin.models.TagCountsModel;
import wateradmin.models.water_ops.ProjectModel;
import wateradmin.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.List;

@Controller
@Mapping("/ops/")
public class ProjectController extends BaseController {

    @Mapping("project")
    public ModelAndView project(String tag_name) throws SQLException {
        List<TagCountsModel> tags = DbWaterProjectApi.getProjectTags();
        viewModel.put("tags", tags);
        if (TextUtils.isEmpty(tag_name) == false) {
            viewModel.put("tag_name", tag_name);
        } else {
            if (tags.isEmpty() == false) {
                viewModel.put("tag_name", tags.get(0).tag);
            } else {
                viewModel.put("tag_name", null);
            }
        }
        return view("ops/project");
    }

    @Mapping("project/inner")
    public ModelAndView projectInner(String tag_name,Integer _state) throws SQLException {
        Integer is_enabled = 1;
        if (_state == null){
            _state = 0;
        }
        if (_state==1){
            is_enabled = 0;
        }
        List<ProjectModel> list = DbWaterProjectApi.getProjectByTagName(tag_name,is_enabled);
        viewModel.put("_state",_state);
        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);
        return view("ops/project_inner");
    }


    @Mapping("project/edit")
    public ModelAndView projectEdit(Integer project_id,String tag_name) throws SQLException {
        if(project_id==null){
            project_id =0;
        }

        ProjectModel project = DbWaterProjectApi.getProjectByID(project_id);
        List<TagCountsModel> tags = DbWaterProjectApi.getProjectTags();

        viewModel.put("tags", tags);
        viewModel.put("project", project);
        return view("ops/project_edit");
    }

    @Mapping("/project/edit/ajax/save")
    public ViewModel saveEdit(Integer project_id, String tag, String name, String git_url, String note,Integer type, String developer) throws SQLException {
        boolean is_admin = Session.current().getIsAdmin()>0;
        if (is_admin == false) {
            return viewModel.code(0, "没有权限！");
        }

        if(project_id == null){
            project_id = 0;
        }

        if(type == null){
            type = 0;
        }

        long result = DbWaterProjectApi.updateProject(project_id, tag, name,note,git_url, type,developer);


        if (result > 0) {
            viewModel.code(1, "保存成功！");
        } else {
            viewModel.code(0, "保存失败！");
        }

        return viewModel;
    }

    @Mapping("project/ajax/disabled")
    public ViewModel disabled(Integer project_id,Integer is_enabled) throws SQLException {
        if (project_id==null || project_id == 0){
            viewModel.code(0,"操作异常，请重试！");
        }
        Boolean result = DbWaterProjectApi.updateProjectStatus(project_id,is_enabled);
        if (result){
            viewModel.code(1,"操作成功！");
        }else{
            viewModel.code(0,"操作失败！");
        }
        return viewModel;
    }
}
