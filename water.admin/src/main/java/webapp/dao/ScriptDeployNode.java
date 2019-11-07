package webapp.dao;

import webapp.dao.db.DbWindApi;
import webapp.models.water_wind.WindOperateModel;
import webapp.models.water_wind.WindScriptModel;

/**
 * @author dhb
 * @date 2018/12/19
 */
public class ScriptDeployNode extends DeployNode {

    private int operateId;

    public ScriptDeployNode(int operateId) {
        this.operateId = operateId;
    }

    @Override
    public int exec(ArgModel map) throws Exception {

        WindOperateModel operate = DbWindApi.getOperateById(operateId);
        WindScriptModel script = DbWindApi.getScriptByID(operate.script_id);
        String args = DbWindApi.getOperateParam(operate.operate_id, operate.script_id);

        EnvModel env = new EnvModel(operate.getTargets());
        env.put("_tag", map.get("_tag"));
        env.put("_project", map.get("_project"));
        env.put("_git_ssh", map.get("_git_ssh"));
        env.put("_version", map.get("_version"));

        note = script.name + " ";

        ShellResult ret = ShellUtil.exec(env, args, script.code);
        String out = ret.output;
        int len = out.length();
        int start = len > 2000 ? (len - 2000) : 0;
        note += ret.output.substring(start, len);

        status = ret.isOk ? 1 : 2;

        return status;
    }

    @Override
    public boolean isEnd() {
        return false;
    }
}
