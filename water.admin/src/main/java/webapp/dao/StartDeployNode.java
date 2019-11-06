package webapp.dao;

/**
 * @author dhb
 * @date 2018/12/19
 */
public class StartDeployNode extends DeployNode {

    @Override
    public int exec(ArgModel args) {
        note = "-- deploy start --";
        return status;
    }

    @Override
    public boolean isEnd() {
        return false;
    }
}
