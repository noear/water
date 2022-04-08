package demoapi;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.cloud.impl.CloudI18nBundleFactory;
import org.noear.solon.i18n.I18nBundleFactory;

/**
 * @author noear 2022/4/8 created
 */
@Configuration
public class Config {
    @Bean
    public I18nBundleFactory i18nBundleFactory(){
        return new CloudI18nBundleFactory();
    }
}
