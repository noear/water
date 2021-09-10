package watersev.dso.etl;

import org.noear.snack.ONode;
import org.noear.water.utils.Datetime;
import org.noear.water.utils.Timecount;
import org.noear.weed.DataItem;
import org.noear.weed.DataList;
import org.noear.weed.IQuery;
import org.noear.weed.SQLBuilder;
import watersev.dso.db.DbPaasApi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//part1.ETL抽取器（负责抽取源数据，并压入队列）
//
public  class EtlExtractor extends EtlEngine.Runnable{

    public EtlExtractor(ONode cfg) throws Exception {
        this.context = new EtlContext(cfg, "source");
    }

    private List<Object> repetKeys = new ArrayList<>();

    @Override
    public void run() throws Exception {
        //0.初始化限额
        if (context.limit == 0) {
            context.limit = 1000;//抽取默认设为1000（以免对源数据产生慢查询）
        }

        //1.判断是否可入队
        if (do1_IsToQueue1()) {

            //2.获取当前时间坐标
            long odlCursor = do2_getCursor();

            //3.通过时间坐标抽取数据源
            DataList dataList = do3_getDataSource(odlCursor);

            if(dataList.getRowCount()==0){
                return;
            }

            long newCursor = getCursor(dataList);

            //4.将原始数据队列推送至队列中
            do4_sendQueue(dataList, odlCursor, newCursor);


            //5.更新时间坐标
            do5_updateCursor(newCursor);
        }else{
            EtlLog.write(context,this.getClass(),"队列1长度已超限数");
        }
    }

    //1.判断是否可入队列1（如果出错，对整个流程无影响）
    private boolean do1_IsToQueue1() {
        //获取当前队列数据堆积数量
        long count = context.dataRd.open1((ru) -> {
            return ru.key(context.queue1).listLen();
        });

        return count < context.limit;       //当前队列数大于etl配置的limit
    }

    //2.获取当前时间坐标（如果出错，对整个流程无影响）
    private long do2_getCursor() throws Exception {
        return DbPaasApi.getEtlCursor(context.id);
    }

    //3.通过时间坐标抽取数据源（如果出错，对整个流程无影响）
    private DataList do3_getDataSource(long cursor) throws Exception {
        Timecount timeCount = new Timecount().start();

        //3.1.SQL模型安全检查
        String upperModel = context.model.toUpperCase();
        if (upperModel.indexOf("UPDATE ") >= 0 ||
                upperModel.indexOf("DELETE ") >= 0 ||
                upperModel.indexOf("TRUNCATE ") >= 0 ||
                upperModel.indexOf(";") >= 0) {
            throw new SQLException("SQL模型不安全！！！");
        }

        //3.2.构建抽取查询器
        IQuery query = null;
        if (upperModel.startsWith("SELECT ")) { //判定属于哪种模式
            //模式A::
            //
            SQLBuilder sql = new SQLBuilder();

            //A.1.对宏定义提供支持
            if (context.model.indexOf("@cursor") > 0) {
                //将参数转为?模式传递（就不需要转为字符串了）
                if (context.cursor_is_id)
                    sql.append(context.model.replace("@cursor", "?"), cursor);
                else
                    sql.append(context.model.replace("@cursor", "?"), new Date(cursor));
            } else {
                sql.append(context.model);
            }

            //A.2.对数据进行限数（充许模型里自带LIMIT）
            if (upperModel.indexOf(" LIMIT ") < 0) {
                sql.append(" LIMIT " + context.limit);
            }

            query = context.db.sql(sql);
        } else {
            //模式B::
            //
            query = context.db.table(context.table)
                    .build((tb) -> {
                        if (context.cursor_is_id)
                            tb.where(context.cursor + ">=?", cursor); //要用>=，避免相同时间的数据不能取到
                        else
                            tb.where(context.cursor + ">=?", new Date(cursor));

                        tb.orderBy(context.cursor + " ASC");
                    })
                    .limit(context.limit)
                    .select(context.model + "," + context.cursor);
        }

        //3.3.抽取数据并返回
        DataList data = query.getDataList();
        int count = data.getRowCount();

        //如果多次为0，就不日志了；减少日志
        if (count != 0 || last_count != 0) {
            EtlLog.write(context, this.getClass(), "成功抽取数量：" + data.getRowCount() + "（" + timeCount.stop(1) + "）");
        }
        last_count = count;

        return data;
    }

    private int last_count =-1;

    //4.将原始数据队列推送至队列中（如果出错，对整个流程无影响；出错时，时间坐标未变）
    private void do4_sendQueue(DataList dataList,long oldCursor, long newCursor) {
        List<Object> tmplist = new ArrayList<>();

        if (dataList.getRowCount() > 0) {
            Timecount timeCount = new Timecount().start();

            long timVal = 0;
            Object keyVal = null;
            List<String> valList = new ArrayList<>();

            for (DataItem item : dataList.getRows()) {
                timVal = item.getVariate(context.cursor).longValue(0l);
                keyVal = item.get(context.key);

                if (oldCursor == timVal) { //如果和旧的时间坐标相等 //要考虑去重
                    if (repetKeys.contains(keyVal)) {
                        tmplist.add(keyVal);
                        continue;
                    }
                }

                valList.add(EtlModel.serialize(item));

                if (newCursor == timVal) { //如果和新的时间坐标相等 //做为下次去重的依据
                    tmplist.add(keyVal);
                }
            }

            context.dataRd.open0((ru) -> {
                ru.key(context.queue1).expire(60 * 60 * 24 * 3).listAddRange(valList);
            });

            String timeTxt = null;
            if (context.cursor_is_id) {
                timeTxt = newCursor + "";
            } else {
                timeTxt = new Datetime(newCursor).toString("yyyy-MM-dd HH:mm:ss");
            }

            EtlLog.write(context, this.getClass(), "成功入队数据：" + valList.size() + "（" + timeCount.stop(1) + "）- newCursor=" + timeTxt);
        }

        repetKeys.clear();
        repetKeys.addAll(tmplist);
    }

    //5.更新时间坐标（如果出错，对整个流程无影响）
    private void do5_updateCursor(long cursor) throws Exception {
        DbPaasApi.setEtlCursor(context.id, cursor);
    }

    private long getCursor(DataList dataList) throws Exception {
        if (dataList.getRowCount() > 0) {
            //获取数据最后一行，提取时间坐标，作为最新坐标
            DataItem dataItem = dataList.getLastRow();
            if (dataItem.exists(context.cursor)) {
                return dataItem.getVariate(context.cursor).longValue(0l);
            } else {
                throw new SQLException("缺少时间戳输出：@" + context.cursor);
            }
        } else {
            return 0l;
        }
    }
}
