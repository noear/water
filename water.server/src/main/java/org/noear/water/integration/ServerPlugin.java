package org.noear.water.integration;

import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.handle.Context;
import org.noear.water.WaterClient;
import org.noear.water.utils.BehaviorUtils;
import org.noear.water.utils.TextUtils;
import org.noear.wood.WoodConfig;

/**
 * @author noear 2021/10/28 created
 */
public class ServerPlugin implements Plugin {
     static final String clz_GritUtil = "org.noear.grit.client.GritUtil";

    @Override
    public void start(AppContext context) {
        initWood();
    }

    private void initWood() {
        Class<?> gritClz = Utils.loadClass(clz_GritUtil);
        final boolean isDebugMode = Solon.cfg().isDebugMode() || Solon.cfg().isFilesMode();
        final boolean isWoodStyle2 = "text2".equals(Solon.cfg().get("water.wood.log.style"));
        final boolean isTrackEnable = Solon.cfg().getBool("water.wood.track.enable", false);
        //final boolean isErrorLogEnable = Solon.cfg().getBool("water.wood.error.log.enable", true);

        if (gritClz == null) {
            //api项目
            WoodConfig.onExecuteAft(cmd -> {
                if(cmd.isLog < 0){
                    return;
                }

                if (isDebugMode) {
                    if (isWoodStyle2) {
                        System.out.println(cmd.toSqlString());
                    } else {
                        System.out.println(cmd.text + "\n" + ONode.stringify(cmd.paramMap()));
                    }
                }

                BehaviorUtils.trackOfPerformance(service_name(), cmd, 1000);

                if (isTrackEnable || cmd.isLog > 0) {
                    String tag = cmd.context.schema();
                    if (TextUtils.isEmpty(tag)) {
                        tag = "sql";
                    }

                    WaterClient.Track.addMeterAndMd5(service_name() + "_sql", tag, cmd.text, cmd.timespan());
                }
            });
        } else {
            //admin 项目
            WoodConfig.onExecuteAft((cmd) -> {
                if(cmd.isLog < 0){
                    return;
                }

                if (isDebugMode) {
                    if (isWoodStyle2) {
                        System.out.println(cmd.toSqlString());
                    } else {
                        System.out.println(cmd.text + "\n" + ONode.stringify(cmd.paramMap()));
                    }
                }



                String sqlUp = cmd.text.toUpperCase();
                String chkUp = "User_Id=? AND Pass_Wd=? AND Is_Disabled=0".toUpperCase();

                if (cmd.timespan() > 2000 || cmd.isLog > 0 || sqlUp.indexOf("INSERT INTO ") >= 0 || sqlUp.indexOf("UPDATE ") >= 0 || sqlUp.indexOf("DELETE ") >= 0 || sqlUp.indexOf(chkUp) >= 0) {
                    Context context = Context.current();

                    if (context != null) {
                        BehaviorUtils.trackOfBehavior(service_name(), cmd, context.userAgent(), context.path(), user_puid() + "." + user_name(), context.realIp());
                    }
                }

                if (isTrackEnable || cmd.isLog > 0) {
                    String tag = cmd.context.schema();
                    if (TextUtils.isEmpty(tag)) {
                        tag = "sql";
                    }

                    WaterClient.Track.trackAndMd5(service_name() + "_sql", tag, cmd.text, cmd.timespan());
                }
            });
        }
    }

    private String service_name() {
        return Solon.cfg().appName();
    }

    //用于作行为记录
    private int user_puid() {
        if (Context.current() != null) {
            String tmp = Context.current().attr("user_puid", "0");
            return Integer.parseInt(tmp);
        } else {
            return 0;
        }
    }

    private String user_name() {
        if (Context.current() != null) {
            return Context.current().attr("user_name", null);
        } else {
            return null;
        }
    }
}
