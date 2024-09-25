package wateradmin.dso;

import lombok.extern.slf4j.Slf4j;
import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.util.ClassUtil;
import org.noear.solon.logging.utils.TagsMDC;
import org.noear.water.config.ServerConfig;
import org.noear.water.utils.BehaviorUtils;
import org.noear.water.utils.TextUtils;
import org.noear.wood.WoodConfig;
import wateradmin.dso.db.DbWaterCfgSafeApi;

/**
 * 行为跟踪初始化
 *
 * @author noear 2021/12/1 created
 */
@Slf4j
public class InitPlugin implements Plugin {
    boolean isDebugMode;
    boolean isWoodStyle2;
    boolean isTrackEnable;
    boolean isErrorLogEnable;

    @Override
    public void start(AppContext context) {
        ClassUtil.loadClass("com.mysql.jdbc.Driver");
        ClassUtil.loadClass("com.mysql.cj.jdbc.Driver");


        isDebugMode = Solon.cfg().isDebugMode() || Solon.cfg().isFilesMode();

        String style = Solon.cfg().get("srww.wood.print.style");
        isWoodStyle2 = "sql".equals(style);
        isTrackEnable = Solon.cfg().getBool("srww.wood.track.enable", isDebugMode);
        isErrorLogEnable = Solon.cfg().getBool("srww.wood.error.log.enable", true);


        initWood();

        try {
            ServerConfig.taskToken = DbWaterCfgSafeApi.getServerTokenOne();
        } catch (Throwable e) {
            log.error("ServerConfig.taskToken init error: {}", e);
        }
    }

    private void initWood() {
        initWoodForAdmin();

        WoodConfig.onException((cmd, err) -> {
            TagsMDC.tag0("wood");

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

    private void initWoodForAdmin() {
        //admin 项目
        WoodConfig.onExecuteAft((cmd) -> {
            if (cmd.isLog < 0) {
                return;
            }

            if (isDebugMode) {
                if (isWoodStyle2) {
                    log.debug(cmd.toSqlString());
                } else {
                    log.debug(cmd.text + "\r\n" + ONode.stringify(cmd.paramMap()));
                }
            }

            Context ctx = Context.current();

            if (ctx == null) {
                return;
            }

            String sqlUp = cmd.text.toUpperCase();

            if (cmd.timespan() > 2000 || cmd.isLog > 0 || sqlUp.contains("INSERT INTO ") || sqlUp.contains("UPDATE ") || sqlUp.contains("DELETE ")) {
                String userDisplayName = getUserDisplayName(ctx);
                String userId = getUserId(ctx);

                BehaviorUtils.trackOfBehavior(Solon.cfg().appName(), cmd, ctx.userAgent(), ctx.pathNew(), userId + "." + userDisplayName, ctx.realIp());
            }

            if (isTrackEnable) {
                String tag = cmd.context.schema();
                if (TextUtils.isEmpty(tag)) {
                    tag = "sql";
                }

                CloudClient.metric().addTimer(Solon.cfg().appName() + "_sql", tag, cmd.text, cmd.timespan());
                //WaterClient.Track.track(service_name() + "_sql", tag, cmd.text, cmd.timespan());
            }
        });
    }

    //用于作行为记录
    public String getUserId(Context ctx) {
        return ctx.attrOrDefault("user_id", "0");
    }

    public String getUserDisplayName(Context ctx) {
        if (ctx != null) {
            return ctx.attr("user_display_name");
        } else {
            return null;
        }
    }
}