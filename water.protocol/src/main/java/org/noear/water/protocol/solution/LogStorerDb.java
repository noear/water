package org.noear.water.protocol.solution;

import org.noear.water.log.Level;
import org.noear.water.protocol.ILogStorer;
import org.noear.water.utils.ext.Fun0;
import org.noear.water.utils.ext.Fun1;
import org.noear.water.utils.ext.Fun1Ex;
import org.noear.weed.DbContext;

public class LogStorerDb implements ILogStorer {

    private Fun1Ex<String,DbContext> _source;
    private Fun0<Long> _id;
    public LogStorerDb(Fun1Ex<String,DbContext> source, Fun0<Long> id){
        _source = source;
        _id = id;
    }

    @Override
    public void append(String logger, Level level, String tag, String tag1, String tag2, String tag3, String summary, Object content, String from) {
        try {
            DbContext db = _source.run(logger);

            db.table(logger).usingExpr(true)
                    .set("log_id", _id.run())
                    .set("level", level.code)
                    .setDf("tag", tag, "")
                    .setDf("tag1", tag1, "")
                    .setDf("tag2", tag2, "")
                    .setDf("tag3", tag3, "")
                    .setDf("summary", summary, "")
                    .setDf("content", content, "")
                    .setDf("from", from, "")
                    .set("log_date", "$DATE(NOW())")
                    .set("log_fulltime", "$NOW()")
                    .insert();

        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
