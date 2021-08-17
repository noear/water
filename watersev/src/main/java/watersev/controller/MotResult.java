package watersev.controller;

import org.noear.snack.ONode;

/**
 * @author noear 2021/8/17 created
 */
public class MotResult {
    public final ONode data;
    public final boolean succeed;

    public MotResult(Object data, boolean succeed) {
        this.data = ONode.loadObj(data);
        this.succeed = succeed;
    }

    public static MotResult failure() {
        return new MotResult(null, false);
    }

    public static MotResult succeed(Object data) {
        return new MotResult(data, true);
    }
}
