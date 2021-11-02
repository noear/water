package watersetup;

import org.noear.solon.Utils;
import org.noear.weed.DbContext;

import java.util.Properties;

/**
 * @author noear 2021/10/31 created
 */
public class Config {
    static {
        Utils.loadClass("com.mysql.jdbc.Driver");
        Utils.loadClass("com.mysql.cj.jdbc.Driver");
    }

    static final String TML_MARK_SERVER = "${server}";
    static final String TML_MARK_SCHEMA = "${schema}";
    static final String TML_JDBC_URL = "jdbc:mysql://${server}/${schema}?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true";

    public static DbContext water;

    public static String getJdbcUrl(String dbServer, String dbSchema) {
        return TML_JDBC_URL.replace(TML_MARK_SERVER, dbServer).replace(TML_MARK_SCHEMA, dbSchema);
    }

    public static Properties getProp(String cfg) {
        Properties prop = Utils.buildProperties(cfg);
        if (prop.size() == 0) {
            return prop;
        }

        checkProp(prop);

        return prop;
    }

    public static DbContext getDb(Properties prop) {
        if (water != null) {
            return null;
        }

        if (prop.size() < 4) {
            System.out.println("[Water] Missing water. DataSource configuration");
            return null;
        }


        checkProp(prop);

        DbContext db = new DbContext(prop);
        db.initMetaData();

        return db;
    }


    private static void checkProp(Properties prop) {
        String dbServer = prop.getProperty("server");
        String dbSchema = prop.getProperty("schema");

        if (Utils.isEmpty(dbSchema)) {
            dbSchema = "water";
            prop.setProperty("schema", dbSchema);
        }

        if (Utils.isNotEmpty(dbServer)) {
            prop.setProperty("url", getJdbcUrl(dbServer, dbSchema));
        }
    }

}
