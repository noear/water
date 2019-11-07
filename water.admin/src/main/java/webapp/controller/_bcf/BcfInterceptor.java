package webapp.controller._bcf;

import org.noear.bcf.BcfClient;
import org.noear.bcf.BcfInterceptorBase;
import org.noear.bcf.models.BcfUserModel;
import org.noear.solon.annotation.XInterceptor;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import org.noear.water.admin.tools.utils.IPUtil;
import org.noear.water.admin.tools.dso.Session;
import webapp.dao.db.DbWaterApi;
import webapp.models.water.WhitelistModel;


@XInterceptor(before = true)
public class BcfInterceptor extends BcfInterceptorBase {

    public int getPUID() {
        return Session.current().getPUID();
    }

    @XMapping("**")
    public void verifyHandle(XContext ctx) throws Exception {
        if(ctx.path().equals("/login")){
            return;
        }

        if(getPUID()==0){
            BcfUserModel user = BcfClient.login("xieyuejia", "rd81310.");
            Session.current().loadModel(user);
        }


        if (ctx.uri().getHost().indexOf("localhost") >= 0) {
            return;
        }

        //IP白名单校验
        String ip = IPUtil.getIP(ctx);
        WhitelistModel ipWhite = DbWaterApi.getIPWhite(ip);

        if (ipWhite.id == 0) {
            ctx.output(ip + ",not is whitelist!");
            ctx.setHandled(true);
            return;
        }

        String uri = ctx.path().toLowerCase();
        if (uri.indexOf("/ajax/pull") > 0) {
            return;
        }

        super.verifyHandle(ctx);
    }
}