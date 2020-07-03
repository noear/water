package wateradmin.controller._bcf;

import org.noear.bcf.BcfClient;
import org.noear.bcf.BcfInterceptorBase;
import org.noear.bcf.XSessionBcf;
import org.noear.bcf.models.BcfUserModel;
import org.noear.solon.XApp;
import org.noear.solon.annotation.XInterceptor;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import wateradmin.dso.Session;
import wateradmin.dso.db.DbWaterCfgApi;
import wateradmin.utils.IPUtil;


@XInterceptor(before = true)
public class BcfInterceptor extends BcfInterceptorBase {

    public int getPUID() {
        return Session.current().getPUID();
    }

    @XMapping("/**")
    public void verifyHandle(XContext ctx) throws Exception {
        if (ctx.path().equals("/login")) {
            return;
        }

        if(XApp.cfg().isDebugMode() && getPUID() == 0){
            BcfUserModel um = BcfClient.login(1);
            Session.current().loadModel(um);
        }

        if (getPUID() > 0) {
            ctx.attrSet("user_puid", "" + XSessionBcf.global().getPUID());
            ctx.attrSet("user_name", XSessionBcf.global().getUserName());
        }


        if (ctx.uri().getHost().indexOf("localhost") < 0) {
            //IP白名单校验
            String ip = IPUtil.getIP(ctx);

            if (DbWaterCfgApi.isWhitelist(ip) == false) {
                ctx.output(ip + ",not is whitelist!");
                ctx.setHandled(true);
                return;
            }
        }


        String uri = ctx.path().toLowerCase();
        if (uri.indexOf("/ajax/pull") > 0) {
            return;
        }

        super.verifyHandle(ctx);
    }
}