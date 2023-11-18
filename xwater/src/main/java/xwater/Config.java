package xwater;

import org.noear.solon.Utils;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.DsUtils;
import org.noear.wood.DbContext;
import org.noear.wood.cache.ICacheServiceEx;
import org.noear.wood.cache.LocalCache;
import xwater.dso.db.DbWaterCfgApi;
import xwater.models.view.water_cfg.ConfigModel;

import java.sql.SQLException;
import java.util.Properties;

/**
 * @author noear 2021/10/31 created
 */
public class Config {
    static {
        Utils.loadClass("com.mysql.jdbc.Driver");
        Utils.loadClass("com.mysql.cj.jdbc.Driver");
    }

    public static final ICacheServiceEx cache = new LocalCache();

    public static final String water_setup_step = "water_setup_step";

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

    public static DbContext getDb(Properties props, boolean pool) {
        if (props.size() < 4) {
            System.out.println("[Water] Missing water. DataSource configuration");
            return null;
        }


        checkProp(props);

        DbContext db = DsUtils.getDb(props, pool);


        if (db.initMetaData()) {
            return db;
        } else {
            //说明初始化未成功
            return null;
        }
    }

    /**
     * 获取配置
     * */
    public static ConfigM getCfg(String tag, String key) {
        try {
            ConfigModel cfg = DbWaterCfgApi.getConfigByTagName(tag, key);

            return new ConfigM(cfg.key, cfg.value, 0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void checkProp(Properties prop) {
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
