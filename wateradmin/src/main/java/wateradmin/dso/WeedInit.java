package wateradmin.dso;

import lombok.extern.slf4j.Slf4j;
import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.annotation.Component;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.handle.Context;
import org.noear.solon.logging.utils.TagsMDC;
import org.noear.water.WaterClient;
import org.noear.water.utils.TextUtils;
import org.noear.weed.WeedConfig;

/**
 * @author noear 2021/12/1 created
 */
@Slf4j
@Component
public class WeedInit implements Plugin {
    boolean isErrorLogEnable = false;
    boolean isTrackEnable = false;
    boolean isDebugMode = false;

    @Override
    public void start(SolonApp app) {
        isErrorLogEnable = true;
        isTrackEnable = Solon.cfg().isDebugMode();
        isDebugMode = Solon.cfg().isDebugMode();

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

                log.debug(cmd.text + "\r\n" + ONode.stringify(cmd.paramMap()));

            }

            if (cmd.isLog < 0) {
                return;
            }

            Context ctx = Context.current();
            String user_name = user_name(ctx);
            int user_puid = user_puid(ctx);


            String sqlUp = cmd.text.toUpperCase();

            if (cmd.timespan() > 2000 || cmd.isLog > 0 || sqlUp.contains("INSERT INTO ")|| sqlUp.contains("UPDATE ") || sqlUp.contains("DELETE ")) {
                WaterClient.Track.trackOfBehavior(service_name(), cmd, ctx.userAgent(), ctx.pathNew(), user_puid + "." + user_name, ctx.realIp());
            }

            if (isTrackEnable) {
                String tag = cmd.context.schema();
                if (TextUtils.isEmpty(tag)) {
                    tag = "sql";
                }

                CloudClient.metric().addMeter(service_name() + "_sql", tag, cmd.text, cmd.timespan());
                //WaterClient.Track.track(service_name() + "_sql", tag, cmd.text, cmd.timespan());
            }
        });
    }

    public String service_name() {
        return Solon.cfg().appName();
    }

    //用于作行为记录
    public int user_puid(Context ctx) {
        if (ctx != null) {
            String tmp = ctx.attr("user_puid", "0");
            return Integer.parseInt(tmp);
        } else {
            return 0;
        }
    }

    public String user_name(Context ctx) {
        if (ctx != null) {
            return ctx.attr("user_name", null);
        } else {
            return null;
        }
    }
}
