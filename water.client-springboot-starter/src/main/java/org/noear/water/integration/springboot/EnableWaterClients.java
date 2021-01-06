package org.noear.water.integration.springboot;

import org.noear.nami.integration.springboot.AutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author noear 2021/1/6 created
 */
@Import(AutoConfiguration.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableWaterClients {
}
