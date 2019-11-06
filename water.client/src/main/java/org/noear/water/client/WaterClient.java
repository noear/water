package org.noear.water.client;

import org.noear.water.client.dso.*;

public class WaterClient {
    /**
     * 配置接口
     * */
    public final static ConfigApi Config = new ConfigApi();
    /**
     * 日志接口
     * */
    public final static LogApi Log = new LogApi();
    /**
     * 消息接口
     * */
    public final static MessageApi Message = new MessageApi();
    /**
     * 注册接口
     * */
    public final static RegistryApi Registry = new RegistryApi();
    /**
     * 追踪接口
     * */
    public final static TrackApi Track = new TrackApi();

    /**
     * 工具包
     * */
    public final static ToolApi Tool = new ToolApi();
}
