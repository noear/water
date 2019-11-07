package webapp.dao.ops;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dhb
 * @date 2018/12/19
 */
@Getter
public abstract class DeployNode {

    public static final int START_NODE = 4;
    public static final int SCRIPT_NODE = 1;
    public static final int INPUT_NODE = 2;
    public static final int END_NODE = 5;
    public static final int LINE_NODE = -1;

    public int status = 1;
    public int nodeId;
    public int nextNodeId;
    public String desc = "";
    public String note = "";

    protected List<Integer> nodes = new ArrayList<>();

    public final void add(int nextNodeId) {
        nodes.add(nextNodeId);
    }

    public final DeployNode next() throws Exception {

        if (nodes.size() == 1) {
            nextNodeId = nodes.get(0);
        }

        return DeployUtil.get(nextNodeId);
    }

    /**
     * 执行部署任务
     * @return 任务执行是否成功
     */
    abstract int exec(ArgModel args) throws Exception;

    /**
     * 判断是否为结束节点
     * @return
     */
    abstract boolean isEnd();
}
