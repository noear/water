package waterapi.dso.interceptor;

import org.noear.solon.annotation.After;
import org.noear.solon.annotation.Before;
import org.noear.solon.extend.validation.ValidateInterceptor;

import java.lang.annotation.*;

/**
 * 记录输入
 * */
@Before({LoggingInterceptor.class})
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {
}
