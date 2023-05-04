package wateradmin.dso.aot;

import com.mysql.jdbc.*;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.mysql.jdbc.log.StandardLogger;
import com.zaxxer.hikari.HikariConfig;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.impl.DefaultJwtParserBuilder;
import org.noear.solon.aot.RuntimeNativeMetadata;
import org.noear.solon.aot.RuntimeNativeRegistrar;
import org.noear.solon.aot.hint.MemberCategory;
import org.noear.solon.core.AopContext;
import wateradmin.models.water_paas.LuffyFileModel;

/**
 * @author noear 2023/5/4 created
 */
//@Component
public class RuntimeNativeRegistrarImpl implements RuntimeNativeRegistrar {
    @Override
    public void register(AopContext context, RuntimeNativeMetadata metadata) {
        metadata.registerResourceInclude("upgrade/.*");
        metadata.registerResourceInclude("com.mysql.jdbc.LocalizedErrorMessages.properties");

        metadata.registerSerialization(LuffyFileModel.class);
        metadata.registerReflection(LuffyFileModel.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_DECLARED_METHODS);

        metadata.registerReflection(PreparedStatement.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        metadata.registerReflection(ResultSetImpl.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        metadata.registerReflection(ResultSetMetaData.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

        metadata.registerReflection(JDBC4ResultSet.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        metadata.registerReflection(JDBC4UpdatableResultSet.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        metadata.registerReflection(JDBC42ResultSet.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        metadata.registerReflection(JDBC42UpdatableResultSet.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        metadata.registerReflection(JDBC42PreparedStatement.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

        metadata.registerReflection(MySQLIntegrityConstraintViolationException.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

        metadata.registerReflection(StandardSocketFactory.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        metadata.registerReflection(StandardLogger.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

        metadata.registerReflection(DefaultJwtBuilder.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        metadata.registerReflection(DefaultJwtParserBuilder.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);


        metadata.registerReflection(HikariConfig.class, MemberCategory.INVOKE_DECLARED_METHODS);
    }
}
