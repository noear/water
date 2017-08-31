package watersev;

import noear.snacks.ONode;
import org.apache.http.util.TextUtils;
import watersev.controller.MotController;
import watersev.controller.MsgController;
import watersev.controller.SevController;
import watersev.controller.SynController;
import watersev.dao.LogUtil;
import watersev.dao.WaterApi;
import watersev.dao.db.DbSevApi;
import watersev.utils.ArgsUtil;
import watersev.utils.ServerUtil;
import watersev.utils.ext.Act0Ext;

import java.util.Date;

/**
 * Created by yuety on 2017/7/18.
 */
public class WatersevApplication {
    public static void main(String[] args) {
        ONode argx = ArgsUtil.getONode(args);

        int dTotal = argx.get("--dtotal").getInt();
        int dIndex = argx.get("--dindex").getInt();
        String sss = argx.get("--sss").getString();

        System.out.print("args::" + argx.toJson() + "\r\n");


        //.派发消息
        if (TextUtils.isEmpty(sss) || sss.indexOf("msg") >= 0) {
            System.out.print("run::msg\r\n");

            new Thread(() -> {
                runTask("msg", 100, () -> {
                    MsgController.main(400, dTotal, dIndex);
                });
            }).start();
        }

        //2.检查服务
        if (TextUtils.isEmpty(sss) || sss.indexOf("sev") >= 0) {
            System.out.print("run::sev\r\n");

            DbSevApi.initServiceState();

            new Thread(() -> {
                runTask("sev", 2000, () -> {
                    SevController.main();
                });
            }).start();
        }

        //3.同步任务
        if (TextUtils.isEmpty(sss) || sss.indexOf("syn") >= 0) {
            System.out.print("run::syn\r\n");

            new Thread(() -> {
                runTask("syn2m", 1000 * 60 * 2, () -> {
                    SynController.main(false);
                });
            }).start();

            new Thread(() -> {
                runTask("syn1s", 1000, () -> {
                    SynController.main(true);
                });
            }).start();
        }

        //4.数据监视
        if (TextUtils.isEmpty(sss) || sss.indexOf("chk") >= 0) {
            System.out.print("run::chk\r\n");

            new Thread(() -> {
                runTask("chk", 1000 * 60 * 1, () -> {
                    MotController.main();
                });
            }).start();
        }


        //5.服务签到
        while (true) {
            try {
                regWater(argx);
                Thread.sleep(2000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void runTask(String tag, int sleep, Act0Ext action) {
        while (true) {
            try {
                Date time_start = new Date();
                System.out.print(tag + "::time_start::" + time_start.toString() + "\r\n");

                action.run();

                Date time_end = new Date();
                System.out.print(tag + "::time_end::" + time_end.toString() + "\r\n");

                if (time_end.getTime() - time_start.getTime() < sleep) {
                    Thread.sleep(sleep);//0.5s
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                LogUtil.error(tag + "::main", ex);

                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

    private static void regWater(ONode argx) {
        try {
            WaterApi.Registry.add("watersev", ServerUtil.getFullAddress(), argx.toJson(), "", 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
