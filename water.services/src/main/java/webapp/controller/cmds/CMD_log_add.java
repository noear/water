package webapp.controller.cmds;


import webapp.dso.db.DbLogApi;

public class CMD_log_add extends CMDBase {

    @Override
    protected boolean isLogging() {
        return false;
    }

    @Override
    protected void cmd_exec() throws Exception {
        String logger = get("logger");

        if (checkParamsIsOk(logger) == false) {
            return;
        }

        String tag = get("tag", "");
        String tag1 = get("tag1", "");
        String tag2 = get("tag2", "");
        String label = get("label");
        String content = get("content");


        DbLogApi.addLog(logger, tag, tag1, tag2, label, content);
        data.set("code", 1);
        data.set("msg", "success");
    }
}
