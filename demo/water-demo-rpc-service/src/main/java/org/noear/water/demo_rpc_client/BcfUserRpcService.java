package org.noear.water.demo_rpc_client;

import org.noear.fairy.annotation.FairyClient;

import java.sql.SQLException;

@FairyClient("demo-rpc-service:/bcf/user")
public interface BcfUserRpcService {
    String sayHello();
    UserModel getUser(Integer puid) throws SQLException;
}
