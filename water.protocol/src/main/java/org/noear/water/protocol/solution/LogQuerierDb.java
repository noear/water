package org.noear.water.protocol.solution;

import org.noear.water.protocol.ILogQuerier;
import org.noear.water.protocol.model.LogModel;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ext.Fun0;
import org.noear.water.utils.ext.Fun1Ex;
import org.noear.weed.DbContext;

import java.util.ArrayList;
import java.util.List;

public class LogQuerierDb implements ILogQuerier {

    private Fun1Ex<String,DbContext> _source;
    public LogQuerierDb(Fun1Ex<String,DbContext> source){
        _source = source;
    }

    @Override
    public List<LogModel> list(String logger, Integer level, int size, String tag, String tag1, String tag2, String tag3, Integer log_date, Long log_id) throws Exception {
        if (TextUtils.isEmpty(logger)) {
            return new ArrayList<>();
        }

        if (log_date == null) {
            log_date = 0;
        }

        if (log_id == null) {
            log_id = 0L;
        }

        if (level == null) {
            level = 0;
        }

        DbContext db = _source.run(logger);

        return db.table(logger)
                .where("1 = 1")
                .andIf(TextUtils.isNotEmpty(tag), "tag = ?", tag)
                .andIf(TextUtils.isNotEmpty(tag1), "tag1 = ?", tag1)
                .andIf(TextUtils.isNotEmpty(tag2), "tag2 = ?", tag2)
                .andIf(TextUtils.isNotEmpty(tag3), "tag3 = ?", tag3)
                .andIf(log_date > 0, "log_date = ?", log_date)
                .andIf(log_id > 0, "log_id <= ?", log_id)
                .andIf(level > 0, "level=?", level)
                .orderBy("log_id desc")
                .limit(size)
                .select("*")
                .getList(LogModel.class);
    }
}
