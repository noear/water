package waterapp.controller.cmds;

import org.noear.water.utils.TextUtils;
import waterapp.dso.TraceUtils;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_sev_track_api extends CMDBase {

    @Override
    protected boolean isTrack() {
        return false;
    }

    @Override
    protected boolean isLogging() {
        return false;
    }

    @Override
    protected void cmd_exec() throws Exception {
        String _node = get("_node");
        String _from = get("_from");

        String service = get("service");
        String tag = get("tag");
        String name = get("name");
        long timespan = getlong("timespan"); //豪秒

        TraceUtils.track(service, tag, name, timespan);

        if (TextUtils.isEmpty(_node) == false) {
            TraceUtils.track("_service", service, _node, timespan);
        }

        if (TextUtils.isEmpty(_from) == false) {
            TraceUtils.track("_from", service, _from, timespan);
        }

        data.set("code", 1);
        data.set("msg", "success");
    }
}
