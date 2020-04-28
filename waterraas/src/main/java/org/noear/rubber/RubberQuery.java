package org.noear.rubber;

import org.noear.weed.DataList;
import org.noear.weed.DbContext;

public final class RubberQuery {
    protected RubberQuery(DbContext db, String sql){
        this.db = db;
        this.sql = sql;
    }
    public String sql;
    private DbContext db;

    public DataList query() throws Exception{
        checkSafe();

        return db.sql(sql).getDataList();
    }

    private void checkSafe() throws Exception{
        String sql2 = sql.toLowerCase();

        if (sql2.indexOf(";") >= 0) //不能有分号
            throw new RubberException("不能有;号，请检查模型构造表达式");

        if (sql2.indexOf("delete ") >= 0 || sql2.indexOf("update ") >= 0 || sql2.indexOf("insert ") >= 0)
            throw new RubberException("只支持查询操作，请检查模型构造表达式");

        if(sql2.indexOf("truncate ") >= 0 || sql2.indexOf(" table ") >= 0){
            throw new RubberException("只支持查询操作，请检查模型构造表达式");
        }
    }
}
