package testapi2;

import org.noear.solon.Solon;
import org.noear.water.integration.springboot.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author noear 2020/12/28 created
 */
@EnableWaterClients
@SpringBootApplication
public class TestApi2App {
    public static void main(String[] args) {
        SpringApplication.run(TestApi2App.class, args);
    }
}
