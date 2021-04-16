package watersev.controller;

import org.noear.solon.annotation.Component;
import org.noear.solon.extend.schedule.IJob;
import org.noear.water.WaterClient;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.TextUtils;
import org.noear.water.utils.Timespan;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.DbContext;
import watersev.dso.AlarmUtil;
import watersev.dso.LogUtil;
import watersev.dso.SqlUtil;
import watersev.dso.db.DbWaterApi;
import watersev.models.water.SynchronousModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 同步控制
 * */
@Component
public final class SynController implements IJob {
    @Override
    public String getName() {
        return "syn";
    }

    @Override
    public int getInterval() {
        return 1000 * 10;
    }

    @Override
    public void exec() throws Exception {
        List<SynchronousModel> list = DbWaterApi.getSyncList();

        for (SynchronousModel task : list) {
            //1.是否有处理脚本
            if (TextUtils.isEmpty(task.source_model)) {
                continue;
            }

            //2.检查是否到已预设的时间
            if(task.last_fulltime != null) {
                long seconds = new Timespan(task.last_fulltime).seconds();
                if (task.interval > seconds) {
                    continue;
                }
            }

            try {
                //3.执行
                long max_id = doExec(task);
                if (max_id < 1) {
                    //3.1.如果执行失败//做特别的记录
                    LogUtil.write(this, task.sync_id+"", task.getTitle(), "-1::maxid=" + max_id);
                }

                //4.再次设定最后执行时间
                DbWaterApi.setSyncLastTime(task.sync_id);

            } catch (Exception ex) {
                ex.printStackTrace();
                //5.如果有异常；记录日志；并报警
                LogUtil.write(this, task.sync_id+"", task.getTitle(), ex.getMessage());
                LogUtil.error(this, task.sync_id+"", task.getTitle(), ex);

                AlarmUtil.tryAlarm(task, false, 0);
            }
        }
    }




    private  long doExec(SynchronousModel task) throws Exception {
        String[] ts = task.target.split("::");
        String[] ss = task.source_model.split("::");

        if (ts.length != 2 || ss.length != 2) {
            return -1;
        }

        String sTk = ss[0].trim();
        String sModelCode = ss[1].trim();

        if(sTk.startsWith("--")){
            sTk = sTk.substring(2).trim();
        }

        ConfigM tc = WaterClient.Config.getByTagKey(ts[0]);
        ConfigM sc = WaterClient.Config.getByTagKey(sTk);

        if (tc == null || sc == null) {
            return -2;
        }

        DbContext tdb = tc.getDb();
        DbContext sdb = sc.getDb();

        if (task.type == 0) {
            //0,增量同步
            return sync_type0(task, sdb, tdb, sModelCode, ts[1]);
        } else {
            //1,更新同步；
            return sync_type1(task, sdb, tdb, sModelCode, ts[1]);
        }
    }

    //0,增量同步
    private  long sync_type0(SynchronousModel task, DbContext sdb, DbContext tdb, String sModelCode, String tsTable) throws Exception {
        long max_id = 0;
        //
        // 1000条为一块，分块读取
        //
        final int block_size = 1000;
        DataList list = null;

        while (true) {
            max_id = tdb.table(tsTable).select("MAX(" + task.target_pk + ")").getVariate().longValue(0l);

            list = getSourceData(task, sdb, sModelCode, max_id, block_size);

            if (list.getRowCount() > 0) {
                tdb.table(tsTable).insertList(list.getRows());
                LogUtil.write(this, task.sync_id + "", task.getTitle(), "1::maxid=" + max_id);
            }


            if (list.getRowCount() < block_size) {
                break;
            } else {
                Thread.sleep(1000 * 1);
            }
        }

        return max_id;
    }

    private  DataList getSourceData(SynchronousModel task, DbContext sdb, String sModelCode, long max_id, int rows) throws SQLException {
        DataList list = null;

        String sql = sModelCode;

        if (SqlUtil.isSafe(sql)) { //如果包函了源表；
            sql = SqlUtil.preProcess(sql);
            sql = sql.replaceAll("@key", max_id + "");

            sql = sql + " LIMIT " + rows;

            list = sdb.sql(sql).getDataList();
        } else {
            LogUtil.write(this, task.sync_id+"", task.name + "（非安全代码）", -1+"::"+sql);
        }

        if (list == null)
            return new DataList();
        else
            return list;
    }


    //1,更新同步；
    private  long sync_type1(SynchronousModel task, DbContext sdb, DbContext tdb, String sModelCode, String tsTable) throws Exception {
        final int block_size = 1000;
        long max_id = task.task_tag;

        DataList list = getSourceData(task, sdb, sModelCode, max_id, block_size);


        if (list.getRowCount() > 0) {
            for (DataItem item : list.getRows()) {
                long key = item.getVariate(task.target_pk).longValue(0l);
                item.remove(task.target_pk);

                if (max_id < key) {
                    max_id = key;
                }

                tdb.table(tsTable).where(task.target_pk + "=?", key).update(item);
            }

            LogUtil.write(this, task.sync_id+"", task.getTitle(), "1::maxid=" + max_id);
        }

        if (list.getRowCount() < block_size) {
            DbWaterApi.setSyncTaskTag(task.sync_id, 0);
        } else {
            DbWaterApi.setSyncTaskTag(task.sync_id, max_id);
        }

        return max_id;
    }
}
