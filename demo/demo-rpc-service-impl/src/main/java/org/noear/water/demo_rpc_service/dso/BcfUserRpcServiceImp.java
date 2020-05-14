package org.noear.water.demo_rpc_service.dso;

import org.noear.solon.annotation.XBean;
import org.noear.solon.annotation.XMapping;
import org.noear.water.annotation.Water;
import org.noear.water.demo_rpc_client.BcfUserRpcService;
import org.noear.water.demo_rpc_client.UserModel;
import org.noear.weed.DbContext;

import java.sql.SQLException;


@XMapping("/bcf/user")
@XBean(remoting = true)
public class BcfUserRpcServiceImp implements BcfUserRpcService {

    @Water("water/water_bcf")
    DbContext bcfDb;

    @Override
    public String sayHello() {
        return "Hello world!";
    }

    @Override
    public UserModel getUser(Integer puid) throws SQLException {
        return bcfDb.table("bcf_user")
                .whereEq("puid",puid).select("*")
                .getItem(UserModel.class);
    }
}
