package org.noear.water;

import org.noear.water.dso.*;

/**
 * Water 客户端
 *
 * @author noear
 * @since 2.0
 * */
public final class WaterClient {
    private static String _localHost = null;
    private static String _localService = null;
    private static String _localServiceHost;
    /**
     * 服务地址 (不能删)
     * */
    public static String localHost(){
        return _localHost;
    }
    public static void localHostSet(String localHost){
        _localHost = localHost;
    }
    /**
     * 服务名
     * */
    public static String localService(){
        return _localService;
    }
    public static void localServiceSet(String localService){
        _localService = localService;
    }
    /**
     * 服务名@服务地址
     * */
    public static String localServiceHost() {
        if (_localService == null || _localHost == null) {
            return null;
        }

        if (_localServiceHost == null) {
            _localServiceHost = _localService + "@" + _localHost;
        }

        return _localServiceHost;
    }

    public static String waterTraceId(){
        return WaterSetting.water_trace_id_supplier().get();
    }

    public static String waterAclToken(){
        return WaterSetting.water_acl_token();
    }

    /**
     * 配置服务接口
     * */
    public final static ConfigApi Config = new ConfigApi();

    /**
     * 密钥服务接口
     * */
    public final static KeyApi Key = new KeyApi();

    /**
     * 国际化服务接口
     * */
    public final static I18nApi I18n = new I18nApi();

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

    /**
     * 任务接口
     * */
    public final static JobApi Job = new JobApi();
}
