package waterapp.controller;

import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;
import waterapp.controller.debug.BlockController;
import waterapp.controller.debug.QueryController;
import waterapp.controller.release.ModelController;
import waterapp.controller.release.SchemeController;

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
