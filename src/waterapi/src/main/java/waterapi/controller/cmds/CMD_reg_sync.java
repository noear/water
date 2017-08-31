package waterapi.controller.cmds;

import waterapi.dao.db.DbApi;

/**
 * Created by yuety on 2017/7/19.
 */
public class CMD_reg_sync extends CMDBase {
    @Override
    protected void cmd_exec() throws Exception {
        String key = get("key");
        int interval = getInt("interval");
        String target = get("target");
        String target_pk = get("target_pk");
        String source = get("source");
        String source_pk = get("source_pk");

        String sync_fields = get("sync_fields");
        int sync_type = getInt("sync_type");

        if (checkParamsIsOk(key, target, target_pk, source, source_pk, sync_fields) == false) {
            return;
        }

        if (interval < 1) {
            data.set("code", 0);
            data.set("msg", "not interval");
            return;
        }

        if (DbApi.hasSync(key)) {
            data.set("code", 0);
            data.set("msg", "has key");
            return;
        }

        DbApi.addSync(key, interval, target, target_pk, source, source_pk, sync_type, sync_fields);

        data.set("code", 1);
        data.set("msg", "success");
    }
}
