package wateradmin.dso.aot;

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


        metadata.registerReflection(DefaultJwtBuilder.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
        metadata.registerReflection(DefaultJwtParserBuilder.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);


        metadata.registerReflection(HikariConfig.class, MemberCategory.INVOKE_DECLARED_METHODS);
    }
}
