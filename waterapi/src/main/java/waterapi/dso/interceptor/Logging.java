package waterapi.dso.interceptor;

import org.noear.solon.annotation.After;

import java.lang.annotation.*;

/**
 * 记录输入
 * */
@After({LoggingInterceptor.class})
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {
}
