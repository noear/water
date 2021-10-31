package watersetup;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.Props;
import org.noear.water.WW;
import org.noear.weed.DbContext;

import javax.sql.DataSource;

public class WatersetupApp {
    static final String TML_MARK_SERVER = "${server}";
    static final String TML_MARK_SCHEMA = "${schema}";
    static final String TML_JDBC_URL = "jdbc:mysql://${server}/${schema}?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true";

    public static void main(String[] args) {
        NvMap argx = NvMap.from(args);

        //支持环境控制
        //
        String waterSetup = System.getenv("water.setup");
        if (waterSetup != null) {
            argx.set("setup", waterSetup);
        }

        System.err.println("[Water] setup mode start...");

        //添加扩展目录(直接使用 waterapi 的扩展目录)
        argx.set("extend", "watersetup_ext");

        SolonApp app = Solon.start(WatersetupApp.class, argx, x -> {
            //加载环境变量(支持弹性容器设置的环境)
            x.cfg().loadEnv("water.");

            String s = argx.get("s");
            String u = argx.get("u");
            String p = argx.get("p");

            if (Utils.isNotEmpty(s) && Utils.isNotEmpty(u) && Utils.isNotEmpty(p)) {
                System.setProperty(WW.cfg_water_ds_schema, "water");
                System.setProperty(WW.cfg_water_ds_url, TML_JDBC_URL.replace(TML_MARK_SERVER, s).replace(TML_MARK_SCHEMA, "water"));
                System.setProperty(WW.cfg_water_ds_username, u);
                System.setProperty(WW.cfg_water_ds_password, p);
            }

            x.enableErrorAutoprint(false);
        });


        //构建数据源
        Props prop = app.cfg().getProp("water.dataSource");
        if(prop.size() == 0){
            prop = app.cfg().getProp("water.ds");
        }

        if (prop.size() < 4) {
            throw new RuntimeException("[Water] Missing water. DataSource configuration");
        }
        String dbServer = prop.getProperty("server");
        String dbSchema = prop.getProperty("schema");
        if (Utils.isNotEmpty(dbServer)) {
            prop.setProperty("url", TML_JDBC_URL.replace(TML_MARK_SERVER, dbServer).replace(TML_MARK_SCHEMA, dbSchema));
        }

        Config.water = new DbContext(prop);
        Config.water.initMetaData();
    }
}