package org.noear.water.demo_api.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.XMethod;
import org.noear.water.annotation.Water;
import org.noear.weed.DbContext;

import java.util.Map;

@XMapping("/bcf/user/")
@XController
public class UserController {

    @Water("water/water_bcf")
    DbContext bcfDb;

    @XMapping(method = XMethod.GET)
    public Map<String, Object> get(Integer puid) throws Exception {
        return bcfDb.table("bcf_user")
                .whereEq("puid",puid)
                .select("*").getMap();
    }
}
