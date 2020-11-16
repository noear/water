package org.noear.water.demo_api.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.XInject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.XMethod;
import org.noear.water.demo_api.dso.db.UserService;
import org.noear.water.demo_api.model.BcfUserModel;

@Mapping("/bcf/user/")
@Controller
public class UserController {
    @XInject
    UserService userService;

    @Mapping(method = XMethod.GET)
    public BcfUserModel get(Integer puid) throws Exception {
        return userService.getUser(puid);
    }
}
