package waterapi.dso.aot;

import com.zaxxer.hikari.HikariConfig;
import org.noear.solon.aot.RuntimeNativeMetadata;
import org.noear.solon.aot.RuntimeNativeRegistrar;
import org.noear.solon.aot.hint.MemberCategory;
import org.noear.solon.core.AopContext;

/**
 * @author noear 2023/5/4 created
 */
//@Component
public class RuntimeNativeRegistrarImpl implements RuntimeNativeRegistrar {
    @Override
    public void register(AopContext context, RuntimeNativeMetadata metadata) {
        metadata.registerResourceInclude("com.mysql.jdbc.LocalizedErrorMessages.properties");

        metadata.registerReflection(HikariConfig.class, MemberCategory.INVOKE_DECLARED_METHODS);
    }
}
