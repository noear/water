package watersev.controller;

import noear.weed.DataItem;
import noear.weed.DataList;
import noear.weed.DbContext;
import org.apache.http.util.TextUtils;
import watersev.dao.LogUtil;
import watersev.dao.SqlUtil;
import watersev.dao.db.DbApi;
import watersev.models.water.ConfigModel;
import watersev.models.water.SynchronousModel;
import watersev.utils.Datetime;

import java.sql.SQLException;
import java.util.List;

public class SynController {
    public static void main(boolean isFast) {
        Datetime time =  Datetime.Now();
        int hours = time.getHours();

        if (hours > 1 && hours < 6) { //半夜不做事
            return;
        }

        List<SynchronousModel> list = DbApi.getSyncList(isFast);

        for (SynchronousModel task : list) {
            if(TextUtils.isEmpty(task.source_model)){
                continue;
            }

            try {
                long max_id = exec(task);
                if (max_id < 1) {
                    LogUtil.doWrite("synchronous",1, task.getTitle(), "maxid=" + max_id);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                LogUtil.doWrite("synchronous", task.getTitle(), ex.getMessage());
                LogUtil.error("synchronous", task.getTitle(), ex);
            }
        }
    }

    private static long exec(SynchronousModel task) throws Exception {
        String[] ts = task.target.split("\\.");
        String[] ss = task.source.split("\\.");

        if (ts.length != 3 || ss.length != 3) {
            return -1;
        }

        ConfigModel tc = DbApi.getConfig(ts[0], ts[1]);
        ConfigModel sc = DbApi.getConfig(ss[0], ss[1]);

        if (tc == null || sc == null) {
            return -2;
        }

        DbContext tdb = tc.getDb();
        DbContext sdb = sc.getDb();

        if (task.type == 0) { //0,增量同步；1,更新同步；
            return sync_type0(task, sdb, tdb, ss, ts);
        } else {
            return sync_type1(task, sdb, tdb, ss, ts);
        }
    }

    private static long sync_type0(SynchronousModel task, DbContext sdb, DbContext tdb, String[] ss, String[] ts) throws Exception {
        long max_id = 0;
        final int rows = 1000;

        while (true) {
            max_id = tdb.table(ts[2]).select("MAX(" + task.target_pk + ")").getVariate().longValue(0l);

            DataList list = getSourceData(task,sdb,ss[2],max_id,rows);


            if (list.getRowCount() > 0) {
                tdb.table(ts[2]).insertList(list.getRows());
                LogUtil.doWrite("synchronous",1, task.getTitle(), "maxid=" + max_id);
            }

            if (list.getRowCount() < rows) {
                break;
            } else {
                Thread.sleep(1000 * 1);
            }
        }

        return max_id;
    }

    private static long sync_type1(SynchronousModel task, DbContext sdb, DbContext tdb, String[] ss, String[] ts) throws Exception {
        final int rows = 1000;
        long max_id = task.task_tag;


        DataList list = getSourceData(task,sdb,ss[2],max_id,rows);


        if (list.getRowCount() > 0) {
            for (DataItem item : list.getRows()) {
                long key = item.getLong2(task.target_pk);
                item.remove(task.target_pk);

                if (max_id < key) {
                    max_id = key;
                }

                tdb.table(ts[2]).where(task.target_pk + "=?", key).update(item);
            }

            LogUtil.doWrite("synchronous", 1, task.getTitle(), "maxid=" + max_id);
        }

        if (list.getRowCount() < rows) {
            DbApi.setSyncTaskTag(task.sync_id, 0);
        } else {
            DbApi.setSyncTaskTag(task.sync_id, max_id);
        }

        return max_id;
    }

    private static DataList getSourceData(SynchronousModel task, DbContext sdb, String sTable,long max_id,int rows) throws SQLException {
        DataList list = null;

        String sql = task.source_model;

        if (sql.indexOf(sTable) > 0 && SqlUtil.isSafe(sql)) { //如果包函了源表；
            sql = SqlUtil.preProcess(sql);
            sql = sql.replaceAll("@key", max_id + "");

            sql = sql + " LIMIT " + rows;

            list = sdb.sql(sql).getDataList();
        }

        if (list == null)
            return new DataList();
        else
            return list;
    }
}
