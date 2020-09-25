package waterapi.dso.interceptor;

import org.noear.solon.annotation.XAfter;
import org.noear.solon.annotation.XBefore;
import org.noear.solon.extend.validation.ValidateInterceptor;

import java.lang.annotation.*;

/**
 * 记录输入
 * */
@XBefore({LoggingInterceptor.class})
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {
}
