package waterapi.controller.cmds;

import waterapi.dao.db.DbLogApi;
import waterapi.utils.HttpUtil;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_log_add extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {
        String logger = get("logger");
        String tag = get("tag");
        String label = get("label");
        String content = get("content");

        if (checkParamsIsOk(logger, tag) == false) {
            return;
        }

        long tag1 = getlong("tag1");
        long tag2 = getlong("tag2");

        DbLogApi.addLog(logger, tag,tag1,tag2 ,label, content);
        data.set("code", 1);
        data.set("msg", "success");
    }
}
