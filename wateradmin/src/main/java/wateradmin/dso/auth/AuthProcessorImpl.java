package wateradmin.dso.auth;

import org.noear.grit.client.GritClient;
import org.noear.solon.core.handle.Context;
import org.noear.solon.extend.grit.GritAuthProcessor;
import wateradmin.dso.Session;

/**
 * @author noear 2021/5/28 created
 */
public class AuthProcessorImpl extends GritAuthProcessor {
    @Override
    protected long getSubjectId() {
        return Session.current().getSubjectId();
    }

    @Override
    public boolean verifyLogined() {
        Context ctx = Context.current();
        if (ctx != null) {
            //跳过 grit rpc 服务
            if (ctx.path().startsWith(GritClient.getRpcPath())) {
                return true;
            }
        }

        return super.verifyLogined();
    }

    @Override
    public boolean verifyIp(String ip) {
        Context ctx = Context.current();
        if (ctx != null) {
            //跳过 grit rpc 服务
            if (ctx.path().startsWith(GritClient.getRpcPath())) {
                return true;
            }
        }

        return super.verifyIp(ip);
    }

    @Override
    public boolean verifyPath(String path, String method) {
        if (path.contains("@") || path.contains("/ajax/pull")) {
            return true;
        }

        if (path.startsWith(GritClient.getRpcPath())) {
            return true;
        }

        return super.verifyPath(path, method);
    }
}
