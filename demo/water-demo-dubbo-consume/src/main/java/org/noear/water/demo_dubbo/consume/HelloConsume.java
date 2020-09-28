package org.noear.water.demo_dubbo.consume;

import org.apache.dubbo.config.annotation.Reference;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.water.demo_dubbo.service.HelloService;
import org.noear.water.demo_dubbo.service.UserService;

@XController
public class HelloConsume {
    @Reference(group = "hello")
    HelloService helloService;

    @Reference
    UserService userService;

    @XMapping("/")
    public String home(){
        return helloService.sayHello(userService.getUser("noear"));
    }
}
