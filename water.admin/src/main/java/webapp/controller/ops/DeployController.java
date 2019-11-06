package webapp.controller.ops;

import com.alibaba.fastjson.JSONArray;
import org.noear.bcf.BcfClient;
import org.noear.bcf.models.BcfUserModel;
import org.noear.snack.ONode;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import webapp.Config;
import webapp.controller.BaseController;
import webapp.dao.ArgModel;
import webapp.dao.DeployNode;
import webapp.dao.DeployTask;
import webapp.dao.DeployUtil;
import webapp.dao.db.DbDeployApi;
import webapp.dao.db.DbWindApi;
import webapp.models.water_wind.WindDeployFlowModel;
import webapp.models.water_wind.WindDeployModel;
import webapp.models.water_wind.WindDeployTaskModel;
import webapp.models.water_wind.WindProjectModel;
import webapp.utils.StringUtil;
import webapp.viewModels.ViewModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author:Yunlong Feng
 * @Description: 服务部署
 * @Date:9:55 2018/12/26
 */
@XController
@XMapping("/ops/")
public class DeployController extends BaseController {

    @XMapping("deploy")
    public ModelAndView deploy(Integer _state) throws Exception {

        int is_over = 0;
        if (_state != null) {
            is_over = _state;
        }

        List<WindDeployTaskModel> deployList = DbWindApi.getDeployTask(is_over);

        viewModel.put("deployList", deployList);
        return view("ops/deploy");
    }

    @XMapping("deploy/edit")
    public ModelAndView createTask() throws SQLException {
        List<WindProjectModel> projectList = DbWindApi.getProjectList();
        List<WindDeployModel> deployList = DbWindApi.getDeployList(projectList.get(0).project_id);
        List<BcfUserModel> pmList = BcfClient.getUsersByGroup(16);
        viewModel.put("pmList", pmList);
        viewModel.put("deployList", deployList);
        viewModel.put("projectList", projectList);
        return view("ops/deploy_edit");
    }

    @XMapping("deploy/ajax/save")
    public ViewModel saveTask(Integer project_id, String product_manager, String version, String note,Integer deploy_id) throws Exception {
        WindProjectModel project = DbWindApi.getProjectByID(project_id);
        boolean result = DbWindApi.setDeployTask(deploy_id, project_id,
                                                 project.name, project.developer,
                                                 product_manager, version, note);

        if (result) {
            viewModel.code(1, "保存成功！");
        } else {
            viewModel.code(0, "保存失败！");
        }
        return viewModel;
    }

    @XMapping("deploy/ajax/getProjectDeploy")
    public JSONArray getProjectDeploy(Integer project_id) throws SQLException{
        List<WindDeployModel> deployList = DbWindApi.getDeployList(project_id);
        return  (JSONArray)JSONArray.toJSON(deployList);
    }

    @XMapping("deploy/operate")
    public ModelAndView operate(Long task_id) throws Exception {

        List<WindDeployFlowModel> logs = DbWindApi.getDeployFlowByTaskId(task_id);
        WindDeployTaskModel task = DbWindApi.getDeployTaskById(task_id);
        WindProjectModel project = DbWindApi.getProjectByID(task.project_id);
        Map<String, Integer> buttons = DeployUtil.next(task_id);

        long lastId = 0L;
        if (logs.size() > 0) {
            lastId = logs.get(logs.size() - 1).flow_id;
        }

        viewModel.put("lastId", lastId);
        viewModel.put("buttons", buttons);
        viewModel.put("project", project);
        viewModel.put("task", task);
        viewModel.put("logs", logs);

        return view("/ops/deploy_operate");
    }

    @XMapping("deploy/ajax/exec")
    public ViewModel exec(Integer task_id, Integer node_id) throws Exception {

        DeployNode node = DeployUtil.get(node_id);

        WindDeployTaskModel task = DbWindApi.getDeployTaskById(task_id);
        WindProjectModel project = DbWindApi.getProjectByID(task.project_id);
        ArgModel args = new ArgModel();
        args.put("_tag", project.tag);
        args.put("_project", project.name);
        args.put("_git_ssh", project.git_url);
        args.put("_version", task.version);

        CompletableFuture.runAsync(() -> {
            try {
                DeployTask.bind(node)
                          .env(args)
                          .logger(d -> DbDeployApi.addDeployFlow(task_id, d))
                          .start();
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        viewModel.code(1, "部署任务提交成功");
        return viewModel;
    }

    @XMapping("deploy/ajax/getLog")
    public ViewModel getLog(Long task_id, Long flow_id) throws SQLException {
        WindDeployFlowModel flow = DbDeployApi.getCurrentFlow(task_id);
        List<WindDeployFlowModel> list = DbDeployApi.getDeployFlowByCurrentId(task_id, flow_id);
        ONode logs = new ONode().asArray();
        list.forEach(log -> {
            ONode data = new ONode();
            data.set("flow_id", log.flow_id);
            data.set("note", log.note);
            logs.add(data);
        });
        viewModel.put("logs", logs.toJson());
        viewModel.put("isEnd", (flow.status == 2 || flow.status == 3 || flow.status == 5) ? 1 : 0);
        return viewModel;
    }


    @XMapping("deploy/next")
    public Map<String, Integer> next(long task_id) throws Exception {

        return DeployUtil.next(task_id);
    }
}
