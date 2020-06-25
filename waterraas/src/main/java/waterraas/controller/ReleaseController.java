package waterraas.controller;

import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import waterraas.controller.debug.BlockController;
import waterraas.controller.debug.QueryController;
import waterraas.controller.release.ModelController;
import waterraas.controller.release.SchemeController;

public class ReleaseController implements XHandler {
    @Override
    public void handle(XContext context) throws Exception {

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
