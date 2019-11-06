package webapp.dao.bcf;

import org.noear.bcf.BcfInterceptorBase;
import org.noear.solon.annotation.XInterceptor;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XContext;
import webapp.dao.Session;
import webapp.dao.db.DbWaterApi;
import webapp.models.water.WhitelistModel;
import webapp.utils.IPUtil;


@XInterceptor(before = true)
public class BcfInterceptor extends BcfInterceptorBase {

    public int getPUID() {
        return Session.current().getPUID();
    }

    @XMapping("/**")
    public void verifyHandle(XContext context) throws Exception {
        //IP白名单校验
        String ip = IPUtil.getIP(context);
        WhitelistModel ipWhite = DbWaterApi.getIPWhite(ip);

        if (ipWhite.row_id == 0) {
            context.output(ip + ",not is whitelist!");
            context.setHandled(true);
            return;
        }

        String uri = context.path().toLowerCase();
        if (uri.indexOf("/ajax/pull") > 0) {
            return;
        }

        super.verifyHandle(context);
    }
}