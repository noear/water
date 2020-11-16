package org.noear.water.demo_dubbo.consume;

import org.apache.dubbo.config.annotation.Reference;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.water.demo_dubbo.service.HelloService;
import org.noear.water.demo_dubbo.service.UserService;

@Controller
public class HelloConsume {
    @Reference(group = "hello")
    HelloService helloService;

    @Reference
    UserService userService;

    @Mapping("/")
    public String home(){
        return helloService.sayHello(userService.getUser("noear"));
    }
}
