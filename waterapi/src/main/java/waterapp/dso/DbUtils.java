package waterapp.dso;

import com.zaxxer.hikari.HikariDataSource;
import org.noear.water.utils.TextUtils;
import org.noear.weed.DbContext;

import java.util.Properties;

public class DbUtils {
    public static DbContext getDb(Properties prop) {
        if (prop.size() < 4) {
            throw new RuntimeException("Data source configuration error!");
        }

        HikariDataSource source = new HikariDataSource();

        String schema = prop.getProperty("schema");
        String url = prop.getProperty("url");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String driverClassName = prop.getProperty("driverClassName");

        if (TextUtils.isEmpty(url) == false) {
            source.setJdbcUrl(url);
        }

        if (TextUtils.isEmpty(username) == false) {
            source.setUsername(username);
        }

        if (TextUtils.isEmpty(password) == false) {
            source.setPassword(password);
        }

        if (TextUtils.isEmpty(schema) == false) {
            source.setSchema(schema);
        }

        if (TextUtils.isEmpty(driverClassName) == false) {
            source.setDriverClassName(driverClassName);
        }

        return new DbContext(schema, source);
    }
}
