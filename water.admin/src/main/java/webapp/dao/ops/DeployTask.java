package webapp.dao.ops;

/**
 * @author dhb
 * @date 2018/12/19
 */
public class DeployTask {

    private ArgModel args;
    private DeployNode node;
    private DeployLogger logger;

    private DeployTask(DeployNode node) {
        this.node = node;
    }

    public static DeployTask bind(DeployNode node) {
        DeployTask task = new DeployTask(node);
        return task;
    }

    public DeployTask env(ArgModel args) {
        this.args = args;
        return this;
    }

    public DeployTask logger(DeployLogger logger) {
        this.logger = logger;
        return this;
    }

    public int start() throws Exception {

        if (this.args == null) {
            throw new IllegalArgumentException("please call env before start");
        }

        while (!node.isEnd()) {
            int code = node.exec(args);
            if (code == 1) {
                log();
                node = node.next();
                continue;
            }
            log();
            return code;
        }
        node.exec(args);
        log();
        return 1;
    }

    private void log() throws Exception {
        if (logger != null) {
            logger.log(node);
        }
    }

    public interface DeployLogger {
        void log(DeployNode node) throws Exception;
    }
}
