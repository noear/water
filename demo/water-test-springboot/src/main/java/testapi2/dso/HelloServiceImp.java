package testapi2.dso;

import org.springframework.stereotype.Component;

/**
 * @author noear 2020/12/29 created
 */
@Component
public class HelloServiceImp implements HelloService {
    @Override
    public String hello() {
        return "hello";
    }
}
