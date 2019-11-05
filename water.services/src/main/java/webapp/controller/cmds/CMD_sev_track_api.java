package webapp.controller.cmds;

import org.noear.water.tools.TextUtils;
import webapp.dso.TrackUtil;

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

        TrackUtil.track(service, tag, name, timespan);
        if (TextUtils.isEmpty(_node) == false) {
            TrackUtil.track("_service", service, _node, timespan);
        }

        if (TextUtils.isEmpty(_from) == false) {
            TrackUtil.track("_from", service, _from, timespan);
        }

        data.set("code", 1);
        data.set("msg", "success");
    }
}
