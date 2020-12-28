package testapi2;

import org.noear.solon.Solon;
import org.noear.solon.extend.servlet.SolonServletFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author noear 2020/12/28 created
 */
@Configuration
@SpringBootApplication
public class TestApi2App {
    public static void main(String[] args) {
        Solon.start(TestApi2App.class, args);
        SpringApplication.run(TestApi2App.class, args);
    }

    @Bean
    public FilterRegistrationBean servletRegistrationBean() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SolonServletFilter());

        return registration;
    }
}
