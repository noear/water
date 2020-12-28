package testapi2;

import org.noear.solon.Solon;
import org.noear.solon.extend.servlet.SolonHttpServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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
    public ServletRegistrationBean servletRegistrationBean() {
        //实例化注册器
        ServletRegistrationBean registration = new ServletRegistrationBean(new SolonHttpServlet(), "/");

        //降低该注册器的优先级，避免int方法中的bean未被加载
        registration.setLoadOnStartup(3);

        return registration;
    }
}
