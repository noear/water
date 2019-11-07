package webapp.controller.ops;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.noear.bcf.BcfClient;
import org.noear.bcf.models.BcfUserModel;


import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;
import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.tools.TextUtils;
import webapp.dao.db.DbWindApi;
import webapp.models.water_wind.*;
import webapp.dao.Session;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@XController
@XMapping("/ops/")
public class ProjectController extends BaseController {

    @XMapping("project")
    public ModelAndView project(String tag_name) throws SQLException {
        List<WindProjectModel> tags = DbWindApi.getProjectTags();
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

    @XMapping("project/inner")
    public ModelAndView projectInner(String tag_name,Integer _state) throws SQLException {
        Integer is_enabled = 1;
        if (_state == null){
            _state = 0;
        }
        if (_state==1){
            is_enabled = 0;
        }
        List<WindProjectModel> list = DbWindApi.getProjectByTagName(tag_name,is_enabled);
        viewModel.put("_state",_state);
        viewModel.put("list", list);
        viewModel.put("tag_name", tag_name);
        return view("ops/project_inner");
    }

    //跳转新增项目配置页面
    @XMapping("project/add")
    public ModelAndView projectAdd(String tag_name) throws SQLException {
//        List<WindProjectModel> tags = DbWindApi.getProjectTags();
//        viewModel.put("tags", tags);
//        viewModel.put("tag_name", tag_name);
//        viewModel.put("project", new WindProjectModel());
//        return view("ops/project_edit");
//
        return projectEdit(0,tag_name); //以后把 add 和 edit 统一起来，不要分开了
    }

    /**
     * @Author:Yunlong Feng
     * @Description:跳转编辑项目配置页面
     * @Date:9:56 2018/12/26
     */
    @XMapping("project/edit")
    public ModelAndView projectEdit(Integer project_id,String tag_name) throws SQLException {
        if(project_id==null){
            project_id =0;
        }

        WindProjectModel project = DbWindApi.getProjectByID(project_id);
        List<WindProjectModel> tags = DbWindApi.getProjectTags();

        List<WindServerModel> testList = DbWindApi.getServerByEnv(0);
        List<WindServerModel> prepareList = DbWindApi.getServerByEnv(1);
        List<WindServerModel> productionList = DbWindApi.getServerByEnv(2);

        List<WindProjectResourceModel> resources = DbWindApi.getResourceById(project.project_id);

        List<WindProjectResourceModel> resource_test = resources.stream().filter(resource -> resource.env_type == 0).collect(Collectors.toList());
        List<WindProjectResourceModel> resource_prepare = resources.stream().filter(resource -> resource.env_type == 1).collect(Collectors.toList());
        List<WindProjectResourceModel> resource_production = resources.stream().filter(resource -> resource.env_type == 2).collect(Collectors.toList());

        List<BcfUserModel> usersByGroup = BcfClient.getUsersByGroup(14);

        WindProjectResourceModel testModel = new WindProjectResourceModel();
        WindProjectResourceModel prepareModel = new WindProjectResourceModel();
        WindProjectResourceModel productionModel = new WindProjectResourceModel();

        if (resource_test.size()>0){
            testModel.domain = resource_test.get(0).domain;
            testModel.port_plan = resource_test.get(0).port_plan;
        }
        if (resource_prepare.size()>0){
            prepareModel.domain = resource_prepare.get(0).domain;
            prepareModel.port_plan = resource_prepare.get(0).port_plan;
        }
        if (resource_production.size()>0){
            productionModel.domain = resource_production.get(0).domain;
            productionModel.port_plan = resource_production.get(0).port_plan;
        }

        viewModel.put("test",testModel);
        viewModel.put("prepare",prepareModel);
        viewModel.put("production",productionModel);
        viewModel.put("users",usersByGroup);
        viewModel.put("resource_test", JSON.toJSONString(resource_test));
        viewModel.put("resource_prepare",JSON.toJSONString(resource_prepare));
        viewModel.put("resource_production",JSON.toJSONString(resource_production));
        viewModel.put("testList",testList);
        viewModel.put("prepareList",prepareList);
        viewModel.put("productionList",productionList);
        viewModel.put("tags", tags);
        viewModel.put("project", project);
        return view("ops/project_edit");
    }

    /**
     * @Author:Yunlong Feng
     * @Description:保存项目编辑
     * @Date:9:56 2018/12/26
     */
    @XMapping("/project/edit/ajax/save")
    public ViewModel saveEdit(Integer project_id, String tag, String name, String git_url, String note,
                              String production_host, String prepare_host, String test_host,
                              String production_port, String prepare_port, String test_port,
                              String [] productionList, String [] prepareList, String [] testList,
                              String [] productionValue, String [] prepareValue, String [] testValue,
                              Integer type, String developer) throws SQLException {
        boolean is_admin = Session.current().getIsAdmin()>0;
        if (is_admin == false) {
            return viewModel.code(0, "没有权限！");
        }

        Long result = DbWindApi.updateProject(project_id, tag, name,note,git_url, type,developer);

        if (project_id == null || project_id == 0){
            project_id = Integer.parseInt(String.valueOf(result));
        }

        if (result>0) {
            //清空之前保存的数据
            DbWindApi.deleteProject(project_id);
            if (productionList != null && productionList.length > 0) {
                for (int i = 0; i < productionList.length; i++) {
                    DbWindApi.setProjectResource(project_id, production_host, production_port, Integer.valueOf(productionList[i]), productionValue[i], 2);
                }
            }
            if (prepareList != null && prepareList.length > 0) {
                for (int i = 0; i < prepareList.length; i++) {
                    DbWindApi.setProjectResource(project_id, prepare_host, prepare_port, Integer.valueOf(prepareList[i]), prepareValue[i], 1);
                }
            }
            if (testList != null && testList.length > 0) {
                for (int i = 0; i < testList.length; i++) {
                    DbWindApi.setProjectResource(project_id, test_host, test_port, Integer.valueOf(testList[i]), testValue[i], 0);
                }
            }
        }

        if (result!=0L) {
            viewModel.code(1, "保存成功！");
        } else {
            viewModel.code(0, "保存失败！");
        }

        return viewModel;
    }

    /**
     * @Author:Yunlong Feng
     * @Description: 禁用或启用项目
     * @Date:10:33 2018/12/19
     */
    @XMapping("project/ajax/disabled")
    public ViewModel disabled(Integer project_id,Integer is_enabled) throws SQLException {
        if (project_id==null || project_id == 0){
            viewModel.code(0,"操作异常，请重试！");
        }
        Boolean result = DbWindApi.updateProjectStatus(project_id,is_enabled);
        if (result){
            viewModel.code(1,"操作成功！");
        }else{
            viewModel.code(0,"操作失败！");
        }
        return viewModel;
    }

    //项目部署流程列表
    @XMapping("project/project_deploy_inner")
    public ModelAndView projectDeployInner(Integer project_id) throws SQLException{

        List<WindDeployModel> deployList = DbWindApi.getDeployList(project_id);
        viewModel.put("deployList",deployList);
        viewModel.put("project_id",project_id);
        return view("ops/project_deploy_inner");
    }

    //项目部署流程编辑
    @XMapping("project/project_deploy_edit")
    public ModelAndView projectDeployEdit(Integer project_id,Integer deploy_id) throws SQLException{

        if (deploy_id==null){
            deploy_id = 0;
            viewModel.put("deploy",new WindDeployModel());
        } else {
            WindDeployModel deploy = DbWindApi.getDeploy(deploy_id);
            viewModel.put("deploy",deploy);
        }
        viewModel.put("project_id",project_id);
        return view("ops/project_deploy_edit");
    }

    //项目部署流程编辑保存
    @XMapping("project/project_deploy_edit/ajax/save")
    public ViewModel saveProjectDeployEdit(Integer project_id,String deploy_name,Integer deploy_id) throws SQLException{

        if (deploy_id==null){
            deploy_id = 0;
        }
        boolean isOk = DbWindApi.setProjectDeploy(project_id, deploy_id, deploy_name);
        if (isOk)
            viewModel.put("code",1);
        else
            viewModel.put("code",0);
        return viewModel;
    }

    //节点流程图编辑
    @XMapping("project/deploy_design")
    public ModelAndView deployDesign(Integer deploy_id,Integer project_id) throws SQLException{
        WindDeployModel deploy = DbWindApi.getDeploy(deploy_id);
        viewModel.put("deploy",deploy);
        viewModel.put("project_id",project_id);
        return view("ops/deploy_design");
    }

    //节点流程图编辑保存
    @XMapping("project/deploy_design/ajax/save")
    public ViewModel saveDeployDesign(Integer project_id,Integer deploy_id,String details) throws Exception{

        viewModel = DbWindApi.setDeployDesign(project_id,deploy_id,details);
        return viewModel;
    }

    //执行节点编辑弹出窗
    @XMapping("project/deploy_node_excute")
    public ModelAndView deployNodeExcute(String node_key) throws SQLException{

        WindDeployNodeModel deployNode = DbWindApi.getDeployNodeByKey(node_key);
        WindOperateModel operate = DbWindApi.getOperateById(deployNode.operate_id);
        viewModel.put("server_id",operate.server_id);

        List<WindServerModel> servers = DbWindApi.getServers(1);
        if (operate.server_id>0){
            List<WindOperateModel> operates = DbWindApi.getOperateByServer(operate.server_id);
            viewModel.put("operates",operates);
        } else {
            if (servers.size()>0){
                List<WindOperateModel> operates = DbWindApi.getOperateByServer(servers.get(0).server_id);
                viewModel.put("operates",operates);
            }
        }

        viewModel.put("servers",servers);
        viewModel.put("node",deployNode);
        return view("ops/deploy_node_excute");
    }

    @XMapping("project/deploy_node_input")
    public ModelAndView deployNodeInput(String node_key) throws SQLException {
        WindDeployNodeModel deployNode = DbWindApi.getDeployNodeByKey(node_key);
        viewModel.put("node",deployNode);
        return view("ops/deploy_node_input");
    }


        @XMapping("project/deploy_node_excute/ajax/changeServer")
    public JSONArray changeServer(Integer server_id) throws SQLException{
        JSONArray out = new JSONArray();
        List<WindOperateModel> operates = DbWindApi.getOperateByServer(server_id);
        if (operates.size()>0){
            out = (JSONArray)JSONArray.toJSON(operates);

        }
        return out;
    }

    //保存执行节点
    @XMapping("project/deploy_node_excute/ajax/saveExcute")
    public boolean saveExcute(Integer deploy_id,String node_id,String name,Integer operate_id,Integer project_id) throws Exception{
        DbWindApi.setDeployNode(deploy_id,node_id,name,operate_id,project_id,1,"","",0);
        return true;
    }

    //保存输入节点
    @XMapping("project/deploy_node_input/ajax/saveInput")
    public boolean saveInput(Integer deploy_id,String node_id,String name,Integer project_id,Integer node_type) throws Exception{
        DbWindApi.setDeployNode(deploy_id,node_id,name,0,project_id,node_type,"","",0);
        return true;
    }
}
