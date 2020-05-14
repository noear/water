package org.noear.water.demo_api.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XInject;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XMethod;
import org.noear.water.demo_api.dso.db.UserService;
import org.noear.water.demo_api.model.BcfUserModel;

@XMapping("/bcf/user/")
@XController
public class UserController {
    @XInject
    UserService userService;

    @XMapping(method = XMethod.GET)
    public BcfUserModel get(Integer puid) throws Exception {
        return userService.getUser(puid);
    }
}
