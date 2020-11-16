package waterraas.controller;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import waterraas.controller.debug.BlockController;
import waterraas.controller.debug.QueryController;
import waterraas.controller.release.ModelController;
import waterraas.controller.release.SchemeController;

public class ReleaseController implements Handler {
    @Override
    public void handle(Context context) throws Exception {

        if (context.param("model") != null) {
            new ModelController().handle(context);
            return;
        }

        if (context.param("block") != null) {
            new BlockController().handle(context);
            return;
        }

        if (context.param("scheme") != null) {
            int type = context.paramAsInt("type");

            if (type > 9) { //10,11
                new QueryController().handle(context);
            } else {
                new SchemeController().handle(context);
            }
            return;
        }
    }
}
