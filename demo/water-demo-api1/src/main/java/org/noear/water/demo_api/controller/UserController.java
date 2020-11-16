package org.noear.water.demo_api.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.XMethod;
import org.noear.water.annotation.Water;
import org.noear.weed.DbContext;

import java.util.Map;

@Mapping("/bcf/user/")
@Controller
public class UserController {

    @Water("water/water")
    DbContext bcfDb;

    @Mapping(method = XMethod.GET)
    public Map<String, Object> get(Integer puid) throws Exception {
        return bcfDb.table("bcf_user")
                .whereEq("puid",puid)
                .select("*").getMap();
    }
}
