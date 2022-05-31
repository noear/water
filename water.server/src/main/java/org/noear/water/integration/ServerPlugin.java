package org.noear.water.integration;

import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.handle.Context;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;
import org.noear.weed.WeedConfig;

/**
 * @author noear 2021/10/28 created
 */
public class ServerPlugin implements Plugin {
     static final String clz_BcfClient = "org.noear.bcf.BcfClient";

    @Override
    public void start(AopContext context) {
        initWeed();
    }

    private void initWeed() {
        Class<?> bcfClz = Utils.loadClass(clz_BcfClient);
        final boolean isDebugMode = Solon.cfg().isDebugMode() || Solon.cfg().isFilesMode();
        final boolean isWeedStyle2 = "text2".equals(Solon.cfg().get("water.weed.log.style"));
        final boolean isTrackEnable = Solon.cfg().getBool("water.weed.track.enable", false);
        //final boolean isErrorLogEnable = Solon.cfg().getBool("water.weed.error.log.enable", true);

        if (bcfClz == null) {
            //api项目
            WeedConfig.onExecuteAft(cmd -> {
                if(cmd.isLog < 0){
                    return;
                }

                if (isDebugMode) {
                    if (isWeedStyle2) {
                        System.out.println(cmd.toSqlString());
                    } else {
                        System.out.println(cmd.text + "\n" + ONode.stringify(cmd.paramMap()));
                    }
                }

                WaterClient.Track.trackOfPerformance(service_name(), cmd, 1000);

                if (isTrackEnable) {
                    String tag = cmd.context.schema();
                    if (TextUtils.isEmpty(tag)) {
                        tag = "sql";
                    }

                    WaterClient.Track.track(service_name() + "_sql", tag, cmd.text, cmd.timespan());
                }
            });
        } else {
            //admin 项目
            WeedConfig.onExecuteAft((cmd) -> {
                if(cmd.isLog < 0){
                    return;
                }

                if (isDebugMode) {
                    if (isWeedStyle2) {
                        System.out.println(cmd.text2());
                    } else {
                        System.out.println(cmd.text + "\n" + ONode.stringify(cmd.paramMap()));
                    }
                }

                Context context = Context.current();

                String sqlUp = cmd.text.toUpperCase();
                String chkUp = "User_Id=? AND Pass_Wd=? AND Is_Disabled=0".toUpperCase();

                if (cmd.timespan() > 2000 || cmd.isLog > 0 || sqlUp.indexOf("INSERT INTO ") >= 0 || sqlUp.indexOf("UPDATE ") >= 0 || sqlUp.indexOf("DELETE ") >= 0 || sqlUp.indexOf(chkUp) >= 0) {
                    WaterClient.Track.trackOfBehavior(service_name(), cmd, context.userAgent(), context.path(), user_puid() + "." + user_name(), context.realIp());
                }

                if (isTrackEnable) {
                    String tag = cmd.context.schema();
                    if (TextUtils.isEmpty(tag)) {
                        tag = "sql";
                    }

                    WaterClient.Track.track(service_name() + "_sql", tag, cmd.text, cmd.timespan());
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
