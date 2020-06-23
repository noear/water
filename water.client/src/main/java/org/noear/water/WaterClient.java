package org.noear.water;

import org.noear.water.dso.*;

/**
 * Water 客户端
 * */
public final class WaterClient {

    //不能删
    public static String localHost = null;
    public static String localService = null;
    private static String _serviceAddr;
    public static String localServiceAddr() {
        if (localService == null || localService == null) {
            return null;
        }

        if (_serviceAddr == null) {
            _serviceAddr = localService + "@" + localHost;
        }

        return _serviceAddr;
    }

    /**
     * 配置服务接口
     * */
    public final static ConfigApi Config = new ConfigApi();

    /**
     * 消息服务接口
     * */
    public final static MessageApi Message = new MessageApi();

    /**
     * 日志服务接口
     * */
    public final static LogApi Log = new LogApi();

    /**
     * 注册服务接口
     * */
    public final static RegistryApi Registry = new RegistryApi();

    /**
     * 跟踪服务接口
     * */
    public final static TrackApi Track = new TrackApi();


    /**
     * 通知接口
     * */
    public final static NoticeApi Notice = new NoticeApi();

    /**
     * 白名单接口
     * */
    public final static WhitelistApi Whitelist = new WhitelistApi();
}
