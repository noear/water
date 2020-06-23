package org.noear.water.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.noear.water.utils.ext.Act1Ex;
import org.noear.water.utils.ext.Fun1Ex;

import java.util.Properties;

public class RabbitMQX {
    String host;
    int port;
    String user;
    String password;

    ConnectionFactory connectionFactory;

    public RabbitMQX(Properties prop) {
        String server = prop.getProperty("server");
        String user = prop.getProperty("user");
        String password = prop.getProperty("password");
        String virtualHost = prop.getProperty("virtualHost");

        if (TextUtils.isEmpty(virtualHost)) {
            virtualHost = "/";
        }

        initDo(server, user, password, virtualHost);
    }

    private void initDo(String server, String user, String password, String virtualHost) {
        if (server.contains(":") == false) {
            throw new RuntimeException("RabbitX:Properties error the server parameter!");
        }
        this.host = server.split(":")[0];
        this.port = Integer.parseInt(server.split(":")[1]);
        this.user = user;
        this.password = password;

        connectionFactory = new ConnectionFactory();

        // 配置连接信息
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(virtualHost);
        if(TextUtils.isEmpty(user) == false) {
            connectionFactory.setUsername(user);
        }
        if(TextUtils.isEmpty(password) == false) {
            connectionFactory.setPassword(password);
        }

        // 网络异常自动连接恢复
        connectionFactory.setAutomaticRecoveryEnabled(true);
        // 每10秒尝试重试连接一次
        connectionFactory.setNetworkRecoveryInterval(10000);
    }

    public void open0(Act1Ex<Channel> action) {
        try {
            try (Connection connection = connectionFactory.newConnection()) {
                try (Channel channel = connection.createChannel()) {
                    action.run(channel);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> T open1(Fun1Ex<Channel, T> action) {
        try {
            try (Connection connection = connectionFactory.newConnection()) {
                try (Channel channel = connection.createChannel()) {
                    return action.run(channel);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
