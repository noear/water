package waterapp.controller;

import org.noear.rubber.Rubber;
import org.noear.solon.core.XContext;
import org.noear.solon.core.XHandler;

public class PreviewController implements XHandler {
    @Override
    public void handle(XContext context) throws Exception {

        if (context.param("model") != null) {
            String model = context.param("model");
            String code = Rubber.modelPreview(model);

            context.contentType("application/x-javascript;charset=UTF-8");
            context.output(code);
            return;
        }

        if (context.param("block") != null) {
            String model = context.param("block");
            String code = Rubber.blockPreview(model);

            context.contentType("application/x-javascript;charset=UTF-8");
            context.output(code);
            return;
        }

        if (context.param("scheme") != null) {
            String scheme = context.param("scheme");
            int type = context.paramAsInt("type");

            String code = null;

            if (type < 10) {
                code = Rubber.schemePreview(scheme);
            } else {
                code = Rubber.schemePreviewAsTsql(scheme);
            }

            context.contentType("application/x-javascript;charset=UTF-8");
            context.output(code);
            return;
        }
    }
}
