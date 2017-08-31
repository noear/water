package waterapi.controller.cmds;

import org.apache.http.util.TextUtils;
import waterapi.dao.db.DbLogApi;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_log_new extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {
        String logger = get("logger");

        if (checkParamsIsOk(logger) == false) {
            return;
        }

        DbLogApi.newLogger(logger);

        data.set("code", 1);
        data.set("msg", "success");
    }
}
