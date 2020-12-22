package watersev.dso.etl;

import org.noear.snack.ONode;
import org.noear.water.utils.TextUtil;
import org.noear.water.utils.Timecount;
import org.noear.weed.DataItem;

//part2.1.ETL转换器（负责将队列数据，转换为目标数据）；作为EtlLoader的工具存在
//
public class EtlTransformer extends EtlEngine.Runnable {
    public EtlTransformer(ONode cfg) throws Exception{
        this.context = new EtlContext(cfg, "target");
    }

    //将队列里的源数据模型转换目标数据模型（如果出错，需要有补尝机制）（已有补尝机制）
    @Override
    public void run() throws Exception {
        //0.初始化限额
        if (context.limit == 0) {
            context.limit = 100;//转换默认设为100（以免数据块过大不能插入）
        }

        context.delContextDataAll();

        if(do1_IsToQueue2()) {
            Timecount timeCount = new Timecount().start();

            int count = 0;
            while (context.limit > count) { //最多超预期数量
                if (do2_transformQueue1() == false) {
                    break;
                }
                count++;
            }

            //如果多次为0，就不日志了；减少日志
            if (count != 0 || last_count != 0) {
                EtlLog.write(context, this.getClass(), "成功转换数量：" + count + " (" + timeCount.stop(1) + ")");
            }
            last_count = count;

        }else{
            EtlLog.write(context,this.getClass(),"队列2长度已超限数");
        }

        context.delContextDataAll();
    }

    private int last_count =-1;

    //1.判断是否可入队列2（如果出错，对整个流程无影响）
    private boolean do1_IsToQueue2() {
        //获取当前队列数据堆积数量
        long count = context.dataRd.open1((ru) -> ru.key(context.queue2).listLen());

        return count < context.limit;       //当前队列数大于etl配置的limit
    }

    //从队列找数据
    private boolean do2_transformQueue1() {
        String jData = null;
        EtlModel sourceModel = null;

        //0.队列取出来（如果出错不用错）
        jData = context.dataRd.open1((ru) -> ru.key(context.queue1).listPop());

        if (TextUtil.isEmpty(jData)) { //如果没有数据了，就停掉拉取
            return false;
        }

        try {
            sourceModel = new EtlModel(EtlModel.unserialize(jData));

            if(sourceModel.data == null) {
                returnQueue1(jData, null);
                EtlLog.write(context, this.getClass(), "反序列化：失败");
                return true;
            }


            //1.转换数据
            DataItem targetItem = do2_1_transformModel(sourceModel);


            //2.检查必要字段
            if (do2_2_checkRequire(targetItem)) {
                //3.1.如果满足必须字段，则到队列2
                do2_3_sendToQueue2(targetItem);
            } else {
                //3.2.如果未满足，则回到队列1
                returnQueue1(jData, sourceModel.data);
                EtlLog.alarm(context, this.getClass(), "未满足条件" , context.json(sourceModel.data, context.require));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            //2.如果转换出错：报警，并将数据发回[抽取队列]（补尝机制）

            EtlLog.error(context, this.getClass(), "转换失败：", ex);

            //先返回队列
            returnQueue1(jData, sourceModel.data);
        }

        return true;
    }



    //2.2.检查必须字段
    private boolean do2_2_checkRequire(DataItem targetItem){
        for (String f : context.require){
            Object val = targetItem.get(f);
            if(val == null || "".equals(val)){
                return false;
            }
        }

        return true;
    }

    //2.3.发送到队列2
    private void do2_3_sendToQueue2(DataItem targetItem) {
        context.dataRd.open0((ru) -> {
            ru.key(context.queue2).expire(60 * 60 * 24 * 3).listAdd(EtlModel.serialize(targetItem));
        });
    }

    //1.转换数据
    private DataItem do2_1_transformModel(EtlModel sourceModel) throws Exception {
        //.封装目标数据
        DataItem targetItem = new DataItem();


        //如果主键在约束字段时，则马上生成；否则暂不生成，在插入时生成
        //
        boolean incKey = context.isKeyBuild;

        if (context.isFullFields) { //支持target.fields = *，即接收全部字段
            for (String k : sourceModel.data.keys()) {
                targetItem.set(k, context.builVal(sourceModel, k, incKey));
            }
        } else {
            for (String k : context.fields) {
                targetItem.set(k, context.builVal(sourceModel, k, incKey));
            }
        }

        return targetItem;
    }

    //>返回队列
    private void returnQueue1(String jData, DataItem dataItem){

        //先入队列 //要不要做3次尝试？
        for(int i=0; i<3; i++){
            try{
                do_returnQueue1(jData, dataItem);
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

    private void do_returnQueue1(String jData, DataItem dataItem){
        context.dataRd.open0((ru)->{
            ru.key(context.queue1).expire(60*60*24*3).listAdd(jData);
        });

        //再写报警日志
        if (dataItem != null) {
            context.alarm(this.getClass(), "返回队列", dataItem);
            EtlLog.write(context, this.getClass(), "返回队列：" + context.queue1 + "：" + context.json(dataItem, context.constraint));
        }
    }
}
