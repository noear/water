package waterapi.dso.interceptor;

import org.noear.solon.annotation.Addition;

import java.lang.annotation.*;

/**
 * 记录输入
 * */
@Addition({LoggingInterceptor.class})
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {
}
