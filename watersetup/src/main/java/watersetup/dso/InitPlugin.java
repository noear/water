package watersetup.dso;

import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.Props;
import org.noear.weed.DbContext;
import watersetup.Config;

/**
 * @author noear 2021/10/31 created
 */
public class InitPlugin implements Plugin {
    static final String TML_MARK_SERVER = "${server}";
    static final String TML_MARK_SCHEMA = "${schema}";
    static final String TML_JDBC_URL = "jdbc:mysql://${server}/${schema}?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true";

    @Override
    public void start(SolonApp app) {

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
