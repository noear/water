package org.noear.water.demo_rpc_client;

import org.noear.solonclient.annotation.XClient;

import java.sql.SQLException;

@XClient("demo-rpc-service:/bcf/user")
public interface BcfUserRpcService {
    String sayHello();
    UserModel getUser(Integer puid) throws SQLException;
}
