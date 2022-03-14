package watersev.controller;
//
//import org.noear.snack.ONode;
//import org.noear.water.utils.EncryptUtil;
//import watersev.dso.AlarmUtil;
//import watersev.dso.LogUtil;
//import watersev.dso.TaskUtil;
//import watersev.dso.db.DbPaasApi;
//import watersev.dso.etl.*;
//import watersev.models.water.PassEtlModel;
//
//import java.util.Date;
//import java.util.List;
//
//public class EtlExtrController implements TaskUtil.ITask, EtlEngine.Handler {
//    @Override
//    public String getName() {
//        return "etlE";
//    }
//
//    @Override
//    public int getInterval() {
//        return 1000 * 30; //0:代表只运行一次 //新加的支持,相当于只是进入这里就完了
//    }
//
//    @Override
//    public void exec() throws Exception {
//        /*
//        * 1.已运行的任务就不再运行
//        * 2.通过60秒检查一次，发现新增加的任务
//        *
//        * */
//
//        List<PassEtlModel> list = DbPaasApi.getEtlList();
//        for (PassEtlModel task : list) {
//            if(task.e_enabled<1) {
//                continue;
//            }
//
//            while (task.e_max_instance > runList.num(task.etl_id)) {
//                runList.start(task.etl_id, () -> {
//                    do_exec(task);
//                });
//            }
//        }
//    }
//
//    private static final EtlTaskCounter runList = new EtlTaskCounter();
//
//    private void do_exec(PassEtlModel task) {
//        ONode cfg = ONode.load(task.code);
//
//        cfg.set("md5", EncryptUtil.md5(task.code));
//        cfg.set("id", task.etl_id);
//        cfg.set("name", task.tag + "_" + task.etl_name);
//        cfg.set("cursor_type",task.cursor_type);
//
//        AlarmUtil.tryNotice("ETL#" + cfg.get("name").getString() + "#提取器，已启动", task.alarm_mobile);
//
//        try {
//            new EtlEngine(new EtlExtractor(cfg))
//                    .handler(this)
//                    .start();
//        }catch (Exception ex) {
//            ex.printStackTrace();
//            runList.stop(task.etl_id);
//            LogUtil.error(this, task.etl_id + "", ex.getMessage(), ex);
//        }
//    }
//
//
//    //EtlEngine.Handler：提供动态控制ETL运行的机制
//    @Override
//    public void run(EtlContext context) throws Exception {
//        PassEtlModel task = DbPaasApi.getEtl(context.id);
//        context.isStop = (task.is_enabled == 0);
//        context.isPause = (task.e_enabled == 0);
//
//        if (context.isStop) {
//            runList.stop(context.id);
//            AlarmUtil.tryNotice("ETL#" + context.name + "#提取器，已停止",task.alarm_mobile);
//            return;
//        }
//
//        if (context.isPause) {
//            if(context.pause_state == 0) {
//                AlarmUtil.tryNotice("ETL#" + context.name + "#提取器，已暂停", task.alarm_mobile);
//            }
//
//            context.pause_state = 1;
//
//            String new_md5 = EncryptUtil.md5(task.code);
//
//            if (context.md5.equals(new_md5) == false) {
//                ONode new_cfg = ONode.load(task.code);
//                new_cfg.set("md5", new_md5);
//
//                context.reloadConfig(new_cfg, EtlExtractor.class);
//                context.reloadFuns(EtlExtractor.class);
//
//                AlarmUtil.tryNotice("ETL#" + context.name + "#提取器，已重新加载配置",task.alarm_mobile);
//            }
//        }else{
//            if(context.pause_state == 1){
//                AlarmUtil.tryNotice("ETL#" + context.name + "#提取器，已重新启动",task.alarm_mobile);
//            }
//
//            context.pause_state = 0;
//
//            DbPaasApi.setEtlExtractTime(task.etl_id, new Date());
//        }
//    }
//}
