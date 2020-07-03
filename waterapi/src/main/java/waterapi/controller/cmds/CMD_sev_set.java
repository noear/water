package waterapi.controller.cmds;

import org.noear.water.utils.TextUtils;
import waterapi.dso.db.DbWaterRegApi;

/**
 * Created by noear on 2017/7/19.
 */
public class CMD_sev_set extends CMDBase {
    @Override
    protected boolean isLogging() {
        return true;
    }

    @Override
    protected void cmd_exec() throws Exception {
        String s = get("s");
        if (TextUtils.isEmpty(s)) {
            String service = get("service");
            String address = get("address");

            int enabled = getInt("enabled", 9);

            if (enabled > 1) {
                return;
            }

            exec0(service, address, enabled);
        } else {
            String[] ss = s.split("@");
            String[] ss2 = ss[1].split(",");

            int enabled = Integer.parseInt(ss2[1]);

            exec0(ss[0], ss2[0], enabled);
        }
    }

    private void exec0(String service, String address, int enabled) throws Exception{


        if (checkParamsIsOk(service, address) == false) {
            return;
        }

        String note = get("note","");

        DbWaterRegApi.disableService(service, address, note, enabled>0);


        data.set("code", 1);
        data.set("msg", "success");
    }
}
