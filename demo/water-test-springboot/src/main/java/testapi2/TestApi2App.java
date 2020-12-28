package testapi2;

import org.noear.solon.Solon;
import org.noear.solon.extend.springboot.SpringBootLinkSolon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author noear 2020/12/28 created
 */
@SpringBootLinkSolon
@SpringBootApplication
public class TestApi2App {
    public static void main(String[] args) {
        Solon.start(TestApi2App.class, args);
        SpringApplication.run(TestApi2App.class, args);
    }
}
