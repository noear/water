package wateradmin.controller._bcf;

import org.noear.bcf.BcfClient;
import org.noear.bcf.BcfInterceptorBase;
import org.noear.bcf.models.BcfUserModel;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.water.WaterClient;
import wateradmin.dso.Session;
import wateradmin.utils.IPUtil;


@Controller
public class BcfInterceptor extends BcfInterceptorBase {

    @Override
    public int getPUID() {
        return Session.current().getPUID();
    }

    @Override
    @Mapping(value = "**", before = true)
    public void verifyHandle(Context ctx) throws Exception {
        if (ctx.path().equals("/login")) {
            return;
        }

        if(Solon.cfg().isDebugMode() && getPUID() == 0){
            BcfUserModel um = BcfClient.login(1);
            Session.current().loadModel(um);
        }

        if (ctx.uri().getHost().indexOf("localhost") < 0) {

            if(Solon.cfg().isWhiteMode()) {
                String ip = IPUtil.getIP(ctx);

                if (WaterClient.Whitelist.existsOfClientAndServerIp(ip) == false) {
                    ctx.output(ip + ",not is whitelist!");
                    ctx.setHandled(true);
                    return;
                }
            }
        }


        String uri = ctx.path().toLowerCase();
        if (uri.indexOf("/ajax/pull") > 0) {
            return;
        }

        super.verifyHandle(ctx);
    }
}