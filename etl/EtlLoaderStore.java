package watersev.dso.etl;

import org.noear.water.utils.TextUtils;
import org.noear.water.utils.ThrowableUtils;
import org.noear.weed.DataItem;

import java.util.List;

//负责加载器的存储策略处理
//
public final class EtlLoaderStore {
    private EtlContext context;
    private EtlLoader loader;

    public EtlLoaderStore(EtlLoader loader){
        this.loader = loader;
        this.context = loader.context;
    }

    public void do2_2_policy(EtlCount etlCount, List<DataItem> loadList, EtlModel dataModel) throws Exception {
        switch (context.policy) {
            case EtlContext.policy1_update_inert1: //1更新且逐条插入;（支持事件）
                do2_policy1(etlCount, loadList, dataModel);
                break;

            case EtlContext.policy2_only_update: //2只更新;（支持事件）
                do2_policy2(etlCount, loadList, dataModel);
                break;

            case EtlContext.policy3_only_pinsert: //3只批量插入;
                do2_policy3(etlCount, loadList, dataModel);
                break;

            case EtlContext.policy4_only_insert1://4只逐条插入（支持事件）
                do2_policy4(etlCount, loadList, dataModel);
                break;

            default: //默认或0:更新且批量插入;
                do2_policy0(etlCount, loadList, dataModel);
                break;
        }
    }

    //0:更新且批量插入;
    private void do2_policy0(EtlCount etlCount, List<DataItem> loadList, EtlModel dataModel) throws Exception{
        if (dataModel.__is_load == 2) { //已存在，需要更新
            if (do2_1_updateData(dataModel.data)) {
                etlCount.stat_update_ok++;
            } else {
                etlCount.stat_update_error++;
            }
            return;
        }

        if (dataModel.__is_load == 0) {
            //尝试锁处理
            if (do2_2_tryLock(dataModel.data) == false) {
                return;
            }
            //最后插入数据库
            loadList.add(dataModel.data);
        }
    }

    //1更新且逐条插入;（支持事件）
    private void do2_policy1(EtlCount etlCount,List<DataItem> loadList, EtlModel dataModel){
        if (dataModel.__is_load == 2) { //已存在，需要更新
            if (do2_1_updateData(dataModel.data)) {
                etlCount.stat_update_ok++;

                do2_1_execEvent(dataModel);//事件（已自带补尝）
            } else {
                etlCount.stat_update_error++;
            }
        }

        if (dataModel.__is_load == 0) {
            //尝试锁处理
            if (do2_2_tryLock(dataModel.data) == false) {
                return;
            }

            //最后插入数据库
            if(do2_1_insertData(dataModel.data)){
                etlCount.stat_insert_ok ++;

                do2_1_execEvent(dataModel);//事件（已自带补尝）
            }else{
                etlCount.stat_insert_error ++;
            }
        }
    }

    //2只更新;（支持事件） //插入的让它回滚
    private void do2_policy2(EtlCount etlCount,List<DataItem> loadList, EtlModel dataModel){
        if (dataModel.__is_load == 2) { //已存在，需要更新
            if (do2_1_updateData(dataModel.data)) {
                etlCount.stat_update_ok++;

                do2_1_execEvent(dataModel);//事件（已自带补尝）
            } else {
                etlCount.stat_update_error++;
            }
        }

        if (dataModel.__is_load == 0) {
            //返回队列（等下次的可能更新）
            loader.returnQueue2(dataModel.data);
        }
    }

    //3只批量插入; //更新的跳过
    private void do2_policy3(EtlCount etlCount, List<DataItem> loadList, EtlModel dataModel) throws Exception {
        if (dataModel.__is_load == 0) {
            //尝试锁处理
            if (do2_2_tryLock(dataModel.data) == false) {
                return;
            }
            //最后插入数据库
            loadList.add(dataModel.data);
        }
    }

    //4只逐条插入（支持事件） //更新的跳过
    private void do2_policy4(EtlCount etlCount,List<DataItem> loadList, EtlModel dataModel){
        if (dataModel.__is_load == 0) {
            //尝试锁处理
            if (do2_2_tryLock(dataModel.data) == false) {
                return;
            }

            //插入数据库
            if(do2_1_insertData(dataModel.data)){
                etlCount.stat_insert_ok ++;

                do2_1_execEvent(dataModel);//事件（已自带补尝）
            }else{
                etlCount.stat_insert_error ++;
            }
        }
    }

    //====================

    private boolean do2_2_tryLock(DataItem dataItem) {
        if (context.lock.length > 0) { //如果有锁
            //生成索的key
            try {
                return context.tryLock(dataItem);
            } catch (Exception ex) {
                ex.printStackTrace(); //如果出现异常，就算锁失败

                EtlLog.error(context, this.getClass(), "do2_2_tryLock", ex);

                loader.returnQueue2(dataItem); //如果锁失败，数据返回顺队列重来

                return false;
            }
        } else {
            return true;
        }
    }

    //更新1个数据项（已添加补尝机制）
    private boolean do2_1_updateData(DataItem dataItem) {
        Object modelKey = dataItem.get(context.key);
        dataItem.remove(context.key);
        try {
            //1.执行更新
            context.db.table(context.table).where(context.key + "=?", modelKey).update(dataItem);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();

            //2.如果出错：报警，并加入（已转换队列）（不是之前的抽取队列）
            EtlLog.error(context, this.getClass(), "更新失败：ID=" + modelKey, ex);

            dataItem.set(context.key, modelKey);
            loader.returnQueue2(dataItem);

            return false;
        }
    }

    //插入1个数据项（已添加补尝机制）
    private boolean do2_1_insertData(DataItem dataItem) {
        Object modelKey = dataItem.get(context.key);

        try {
            //1.执行更新
            context.db.table(context.table).insert(dataItem);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();

            //2.如果出错：报警，并加入（已转换队列）（不是之前的抽取队列）
            EtlLog.error(context, this.getClass(), "插入失败：ID=" + modelKey, ex);

            loader.returnQueue2(dataItem);

            return false;
        }
    }

    private void do2_1_execEvent(EtlModel dataModel){
        if(TextUtils.isEmpty(context.event) == false) {
            Object modelKey = dataModel.data.get(context.key);

            try {
                //1.执行事件
                context.eval(context.event, dataModel);
            } catch (Exception ex) {
                ex.printStackTrace();

                //2.如果出错：报警，并加入（已转换队列）（不是之前的抽取队列）
                EtlLog.error(context, this.getClass(), "事件失败：ID=" + modelKey, ex);

                loader.returnQueue2(dataModel.data);
            }
        }
    }

    public void do2_3_pinsertData(EtlCount etlCount, List<DataItem> dataList) {
        try {
            //1.尝试插入
            if (dataList.size() > 0) {
                context.db.table(context.table).insertList(dataList);
                etlCount.stat_insert_ok = dataList.size();
            }

        } catch (Exception ex) {
            ex.printStackTrace();

            //2.如果出错：报警，并加入（已转换队列）（不是之前的抽取队列）
            StringBuilder sb = new StringBuilder();
            sb.append("ID = (");
            dataList.forEach(m -> {
                sb.append(m.get(context.key)).append(",");
            });
            sb.append(") \r\n");
            sb.append(ThrowableUtils.getString(ex));

            EtlLog.error(context, this.getClass(), "插入失败：" + dataList.size() + "条", sb.toString());
            //写报警日志
            for (DataItem dataItem : dataList) {
                loader.returnQueue2(dataItem);
            }
        }
    }



}
