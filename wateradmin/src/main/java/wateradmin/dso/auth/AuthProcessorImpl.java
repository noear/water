package wateradmin.dso.auth;

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
}
