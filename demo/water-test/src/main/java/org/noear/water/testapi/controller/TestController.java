package org.noear.water.testapi.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;


@XController
public class TestController {
    @XMapping("/")
    public String home() {
        return "OK";
    }
}
