package webapp.dao.ops;

/**
 * @author dhb
 * @date 2018/12/19
 */
public class EndDeployNode extends DeployNode {

    @Override
    public int exec(ArgModel args) {
        note = "-- deploy end --";
        return (status = 5);
    }

    @Override
    public boolean isEnd() {
        return true;
    }
}
