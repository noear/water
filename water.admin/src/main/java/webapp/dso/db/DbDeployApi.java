package webapp.dso.db;

import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.DbContext;
import webapp.Config;
import webapp.dao.DeployNode;
import webapp.models.water_wind.WindDeployFlowModel;

import java.sql.SQLException;
import java.util.List;

/**
 * @author dhb
 * @date 2018/12/19
 */
public class DbDeployApi {

    private static DbContext db() {
        return Config.water;
    }

    public static void addDeployFlow(long taskId, DeployNode node) throws SQLException {

        db().table("wind_deploy_flow")
            .set("task_id", taskId)
            .set("node_id", node.getNodeId())
            .set("status", node.getStatus())
            .set("note", node.getNote())
            .set("desc", node.getDesc())
            .updateExt("task_id,node_id");
    }

    public static WindDeployFlowModel getCurrentFlow(long taskId) throws SQLException {

        WindDeployFlowModel flow = db().table("wind_deploy_flow")
                                       .where("task_id = ?", taskId)
                                       .orderBy("flow_id DESC")
                                       .limit(1)
                                       .select("*")
                                       .getItem(new WindDeployFlowModel());

        return flow;
    }

    public static DataList getDeployNode(int nodeId) throws SQLException {

        return db().table("wind_deploy_node")
                   .where("node_id = ?", nodeId)
                   .select("*")
                   .getDataList();
    }

    /**
     * @Author:Yunlong Feng
     * @Description:
     * @Date:15:25 2018/12/28
     */
    public static List<WindDeployFlowModel> getDeployFlowByCurrentId(Long task_id, Long flow_id) throws SQLException {
        return db().table("wind_deploy_flow")
                   .where("task_id = ?", task_id)
                   .expre(tb -> {
                       if (flow_id != null && flow_id > 0) {
                           tb.and("flow_id > ?", flow_id);
                       }
                   })
                   .orderBy("flow_id ASC")
                   .select("*")
                   .getList(new WindDeployFlowModel());
    }

    public static DataItem getStartNode(int deploy_id) throws SQLException {
        return db().table("wind_deploy_node")
                   .where("deploy_id = ?", deploy_id)
                   .and("node_type = ?", 4)
                   .select("*")
                   .getDataItem();
    }
}
