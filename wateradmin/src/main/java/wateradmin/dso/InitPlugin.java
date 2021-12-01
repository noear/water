package wateradmin.dso;

import lombok.extern.slf4j.Slf4j;
import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.handle.Context;
import org.noear.solon.logging.utils.TagsMDC;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;
import org.noear.weed.WeedConfig;

/**
 * 行为跟踪初始化
 *
 * @author noear 2021/12/1 created
 */
@Slf4j
public class InitPlugin implements Plugin {
    boolean isDebugMode;
    boolean isWeedStyle2;
    boolean isTrackEnable;
    boolean isErrorLogEnable;

    @Override
    public void start(SolonApp app) {
        Utils.loadClass("com.mysql.jdbc.Driver");
        Utils.loadClass("com.mysql.cj.jdbc.Driver");


        isDebugMode = Solon.cfg().isDebugMode() || Solon.cfg().isFilesMode();

        String style = Solon.cfg().get("srww.weed.print.style");
        isWeedStyle2 = "sql".equals(style);
        isTrackEnable = Solon.cfg().getBool("srww.weed.track.enable", isDebugMode);
        isErrorLogEnable = Solon.cfg().getBool("srww.weed.error.log.enable", true);


        initWeed();
    }

    private void initWeed() {
        initWeedForAdmin();

        WeedConfig.onException((cmd, err) -> {
            TagsMDC.tag0("weed");

            if (isErrorLogEnable) {
                if (cmd == null) {
                    log.error("::Error= {}", err);
                } else {
                    log.error("::Sql= {}\n::Args= {}\n\n::Error= {}", cmd.text, ONode.stringify(cmd.paramMap()), err);
                }
            } else {
                if (cmd == null) {
                    log.debug("::Error= {}", err);
                } else {
                    log.debug("::Sql= {}\n::Args= {}\n\n::Error= {}", cmd.text, ONode.stringify(cmd.paramMap()), err);
                }
            }
        });
    }

    private void initWeedForAdmin() {
        //admin 项目
        WeedConfig.onExecuteAft((cmd) -> {
            if (isDebugMode) {
                if (isWeedStyle2) {
                    log.debug(cmd.toSqlString());
                } else {
                    log.debug(cmd.text + "\r\n" + ONode.stringify(cmd.paramMap()));
                }
            }

            if (cmd.isLog < 0) {
                return;
            }

            Context ctx = Context.current();

            if (ctx == null) {
                return;
            }

            String sqlUp = cmd.text.toUpperCase();

            if (cmd.timespan() > 2000 || cmd.isLog > 0 || sqlUp.contains("INSERT INTO ") || sqlUp.contains("UPDATE ") || sqlUp.contains("DELETE ")) {
                String userDisplayName = getUserDisplayName(ctx);
                String userId = getUserId(ctx);

                WaterClient.Track.trackOfBehavior(Solon.cfg().appName(), cmd, ctx.userAgent(), ctx.pathNew(), userId + "." + userDisplayName, ctx.realIp());
            }

            if (isTrackEnable) {
                String tag = cmd.context.schema();
                if (TextUtils.isEmpty(tag)) {
                    tag = "sql";
                }

                CloudClient.metric().addMeter(Solon.cfg().appName() + "_sql", tag, cmd.text, cmd.timespan());
                //WaterClient.Track.track(service_name() + "_sql", tag, cmd.text, cmd.timespan());
            }
        });
    }

    //用于作行为记录
    public String getUserId(Context ctx) {
        return ctx.attr("user_id", "0");
    }

    public String getUserDisplayName(Context ctx) {
        if (ctx != null) {
            return ctx.attr("user_display_name", null);
        } else {
            return null;
        }
    }
}