package org.noear.water.demo_dubbo.consume.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.water.demo_dubbo.service.HelloService;

@XController
public class HelloController {
    @Reference
    HelloService helloService;

    @XMapping("/")
    public String home(){
        return helloService.sayHello("noear");
    }
}
