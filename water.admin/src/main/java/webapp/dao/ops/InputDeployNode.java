package webapp.dao.ops;

/**
 * @author dhb
 * @date 2018/12/19
 */
public class InputDeployNode extends DeployNode {

    @Override
    public int exec(ArgModel args) {
        note = "-- select --";
        return (status = 3);
    }

    @Override
    public boolean isEnd() {
        return false;
    }
}
