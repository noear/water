package watersev.dso.etl;

import org.noear.snack.ONode;
import org.noear.water.utils.TextUtil;
import org.noear.water.utils.Timecount;
import org.noear.weed.DataItem;

import java.util.ArrayList;
import java.util.List;

//par2.ETL加载器（负责将数据加载到目标存储）
//
public  class EtlLoader extends EtlEngine.Runnable {
    private EtlLoaderStore store = null;
    public EtlLoader(ONode cfg) throws Exception{
        this.context = new EtlContext(cfg, "target");
        this.store   = new EtlLoaderStore(this);
    }

    @Override
    public void run() throws Exception {
        //0.初始化限额
        if (context.limit == 0) {
            context.limit = 100;//加载默认设为100（以免数据块过大不能插入）
        }

        context.delContextDataAll();

        //1.读取数据
        List<EtlModel> list = do1_read();

        if (list.size() > 0) {

            if(context.policy<10) {
                //2.执行数据存储
                do2_execStore(list);
            }else{
                //3.执行流计算
                do3_execStream(list);
            }
        }

        context.delContextDataAll();
    }

    //1.将队列里的源数据模型转换目标数据模型（如果出错，需要有补尝机制）（已内建补尝机制）
    private List<EtlModel> do1_read() {
        Timecount timeCount = new Timecount().start();

        String jData = null;
        EtlModel jModel = null;
        List<EtlModel> dataList = new ArrayList<>();

        while (context.limit > dataList.size()) {
            //0.队列取出来（如果出错不用错）
            jData = context.dataRd.open1((ru) -> ru.key(context.queue2).listPop() );

            if (TextUtil.isEmpty(jData)) { //如果没有数据了，就停掉拉取
                break;
            }

            jModel = new EtlModel(EtlModel.unserialize(jData)); //不考虑序列化失败的问题?!

            jModel.data.remove("__is_load");

            dataList.add(jModel);
        }

        int count = dataList.size();

        //如果多次为0，就不日志了；减少日志
        if (count != 0 || last_count != 0) {
            EtlLog.write(context, this.getClass(), "成功读取：" + dataList.size() + "（" + timeCount.stop(1) + "）");
        }
        last_count = count;

        return dataList;
    }

    private int last_count =-1;


    //2.加载数据（如果出错，需要有补尝机制）
    private void do2_execStore(List<EtlModel> list) throws Exception {
        Timecount timeCount = new Timecount().start();

        List<DataItem> loadList = new ArrayList<>();
        EtlCount etlCount = new EtlCount();

        for (EtlModel dataModel : list) {
            try {
                do2_0_checkData(dataModel);
            }catch (Exception ex) {
                returnQueue2(dataModel.data);
                EtlLog.error(context, this.getClass(), "", ex);
            }

            if (dataModel.__is_load == 1) {
                //已存在，但是不需要更新
                etlCount.stat_jump++;
                continue;
            }

            store.do2_2_policy(etlCount,loadList,dataModel);
        }

        store.do2_3_pinsertData(etlCount, loadList);

        StringBuilder sb = new StringBuilder();
        sb.append("成功加载：").append(list.size())
                .append("（新增=").append(etlCount.stat_insert_ok).append(",").append(etlCount.stat_insert_error)
                .append("; 修改=").append(etlCount.stat_update_ok).append(",").append(etlCount.stat_update_error)
                .append("; 跳过=").append(etlCount.stat_jump).append("）（").append(timeCount.stop(1)).append("）");

        EtlLog.write(context, this.getClass(), sb.toString());
    }

    private void do2_0_checkData(EtlModel targetModel) throws Exception {
        //默认认为需要插入
        targetModel.__is_load = 0;

        if (context.constraint.length > 0) {
            //生成约束标识（用于记录日志）
            StringBuilder cks = new StringBuilder();
            for (String k : context.constraint) {
                cks.append(k).append("=").append(targetModel.getString(k)).append("; ");
            }

            //检查数据是否存在
            String selectFileds = context.key + "," + String.join(",", context.constraint);
            if (TextUtil.isEmpty(context.cursor) == false) {
                selectFileds += "," + context.cursor;
            }

            DataItem selectItem = context.db.table(context.table).build((tb) -> {
                tb.where("1=1");
                for (String k : context.constraint) {
                    tb.and(k + "=?", targetModel.get(k));
                }
            }).select(selectFileds).getDataItem();

            //检查数据是否需要更新
            if (selectItem.count() > 0) {
                if(TextUtil.isEmpty(context.cursor)){
                    //跳过处理；（目标数据更新）//没有比较列，不需要处理
                    targetModel.__is_load = 1;
                }else {
                    if (context.policy == EtlContext.policy2_only_update) { //如果是附加数据，则直接设为更新
                        targetModel.__is_load = 2;
                    } else {
                        //如果有数据存在，则更新目标数据的主键值 //如果之前有生成就浪费了***?
                        targetModel.data.set(context.key, selectItem.get(context.key));

                        if (do2_0_isUpdate(targetModel.data, selectItem)) {
                            //需要更新；（目标数据较旧）
                            targetModel.__is_load = 2;
                        } else {
                            //跳过处理；（目标数据更新）
                            targetModel.__is_load = 1;
                        }

                    }
                }
            }
        }

        //确定为插入时，才生成需要动态生成的主键(免费浪费ID资源)***
        if (targetModel.__is_load == 0) {
            if (targetModel.get(context.key) == null) {
                //如果主键还没有，重新生成主键
                targetModel.data.set(context.key, context.builVal(targetModel, context.key, true));
            }
        }
    }


    //3.执行后续事件（如果出错，需要有补尝机制？？？）
    private void do3_execStream(List<EtlModel> list) throws Exception {
        if (TextUtil.isEmpty(context.stream) == false) {
            Exception last_ex = null;
            if(context.policy == EtlContext.policy12_stream_batch){
                try {
                    context.eval(context.stream, list); //批处理无事务
                }catch (Exception ex){
                    EtlLog.error(context, this.getClass(), "do3_execStream", ex);
                }
            }else {
                for (EtlModel dataModel : list) {
                    try {
                        context.eval(context.stream, dataModel);
                    } catch (Exception ex) {
                        ex.printStackTrace();

                        if (context.policy == EtlContext.policy11_stream_tran) {
                            returnQueue2(dataModel.data);
                        }

                        last_ex = ex;
                    }
                }

                if (last_ex != null) {
                    EtlLog.error(context, this.getClass(), "do3_execStream", last_ex);
                }
            }
        }
    }


    //>判断是否需要更新替换（target 已转换的数据,selectItem 查询数据, jTimestamp 字段）
    protected boolean do2_0_isUpdate(DataItem target, DataItem selectItem)  {
        if (TextUtil.isEmpty(context.cursor))
            return true;

        long targetTime = target.getVariate(context.cursor).longValue(0l);
        long selectTime = selectItem.getVariate(context.cursor).longValue(0l);

        if (targetTime == 0) { //如果来源数据没有时间戳，则不更新
            return false;
        }

        if (selectTime == 0) { //如果来目标数据没有时间戳，则更新
            return true;
        }

        return targetTime > selectTime; //XXX::这个判断应该之前应该写反了
    }


    protected void returnQueue2(DataItem dataItem) {
        //先入队列 //要不要做3次尝试？
        for(int i=0; i<3; i++){
            try{
                do_returnQueue2(dataItem);
                return;
            }catch (Exception ex){
                ex.printStackTrace();


                try {
                    Thread.sleep(100);
                }catch (Exception ex2){
                    ex2.printStackTrace();
                }
            }
        }

    }

    private void do_returnQueue2(DataItem dataItem){
        context.dataRd.open0((ru) -> {
            ru.key(context.queue2).expire(60 * 60 * 24 * 3).listAdd(EtlModel.serialize(dataItem));
        });

        //再写报警日志
        try {
            context.alarm(this.getClass(), "返回队列2", dataItem);
            EtlLog.write(context, this.getClass(), "返回队列2：" + ONode.stringify(dataItem.getMap()));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}