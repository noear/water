package org.noear.water.client;

import org.noear.water.client.dso.*;

public class WaterClient {
    /**
     * 配置接口
     * */
    public final static ConfigApi config = new ConfigApi();
    /**
     * 日志接口
     * */
    public final static LogApi log = new LogApi();
    /**
     * 消息接口
     * */
    public final static MessageApi message = new MessageApi();
    /**
     * 注册接口
     * */
    public final static RegistryApi registry = new RegistryApi();
    /**
     * 追踪接口
     * */
    public final static TrackApi track = new TrackApi();
}
