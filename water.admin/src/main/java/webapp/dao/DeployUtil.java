package webapp.dao;

import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import webapp.dao.db.DbDeployApi;
import webapp.dao.db.DbWindApi;
import webapp.models.water_wind.WindDeployFlowModel;
import webapp.models.water_wind.WindDeployTaskModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 部署流程执行工具
 *
 * @author dhb
 * @date 2018/12/19
 */
public class DeployUtil {

    public static DeployNode get(int nodeId) throws SQLException {

        DataList dl = DbDeployApi.getDeployNode(nodeId);
        DataItem di = dl.getRow(0);

        int nodeType = di.getInt("node_type");
        String desc = di.getString("note");

        //对线节点特殊处理一下
        if(nodeType==DeployNode.LINE_NODE){
            int next_node_id = di.getInt("next_node_id");
            DataList node = DbDeployApi.getDeployNode(next_node_id);
            DataItem _node = node.getRow(0);
            nodeType = _node.getInt("node_type");
            desc = _node.getString("note");
        }

        final DeployNode node;
        switch (nodeType) {
            case DeployNode.START_NODE:
                node = new StartDeployNode();
                break;
            case DeployNode.SCRIPT_NODE:
                node = new ScriptDeployNode(di.getInt("operate_id"));
                break;
            case DeployNode.INPUT_NODE:
                node = new InputDeployNode();
                break;
            case DeployNode.END_NODE:
                node = new EndDeployNode();
                break;
            default:
                node = null;
                break;
        }

        node.desc = desc;
        node.nodeId = nodeId;
        node.note = di.getVariate("note").value("");

        dl.forEach(d -> node.add(d.getInt("next_node_id")));

        return node;
    }

    public static Map<String, Integer> next(long taskId) throws Exception {

        Map<String, Integer> map = new HashMap<>(16);

        WindDeployFlowModel flow = DbDeployApi.getCurrentFlow(taskId);
        if (flow.flow_id == 0) {
            WindDeployTaskModel task = DbWindApi.getDeployTaskById(taskId);
            DataItem di = DbDeployApi.getStartNode(task.deploy_id);
            DeployNode node = get(di.getInt("node_id"));
            map.put(node.note, node.nodeId);
        } else {
            if (flow.status == 0 || flow.status == 2) {
                DeployNode node = get(flow.node_id);
                map.put(node.note, node.nodeId);
            } else if (flow.status == 1) {
                DeployNode node = get(flow.node_id);
                DeployNode next = node.next();
                map.put(next.note, next.nodeId);
            } else if (flow.status == 3) {
                DataList dl = DbDeployApi.getDeployNode(flow.node_id);
                dl.forEach(d -> map.put(d.getString("note"), d.getInt("next_node_id")));
            }
        }

        return map;
    }
}
