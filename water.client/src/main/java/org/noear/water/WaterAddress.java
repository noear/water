package org.noear.water;

import org.noear.water.model.LoadBalanceM;
import org.noear.water.utils.TextUtils;

/**
 * Water 内部服务地址
 *
 * @author noear
 * @since 2.0
 */
public class WaterAddress {
    private static LoadBalanceM cfgApiUrl;
    private static LoadBalanceM msgApiUrl;
    private static LoadBalanceM logApiUrl;
    private static LoadBalanceM defApiUrl;

    public static void init(String url) {
        defApiUrl = new LoadBalanceM(url.split(","));

        cfgApiUrl = defApiUrl;
        msgApiUrl = defApiUrl;
        logApiUrl = defApiUrl;
    }

    /**
     * 默认服务地址
     */
    public static LoadBalanceM getDefApiUrl() {
        return defApiUrl;
    }

    public static void setDefApiUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        defApiUrl = new LoadBalanceM(url.split(","));
    }

    /**
     * 配置服务地址
     */
    public static LoadBalanceM getCfgApiUrl() {
        return cfgApiUrl;
    }

    public static void setCfgApiUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        cfgApiUrl = new LoadBalanceM(url.split(","));
    }

    /**
     * 消息服务地址
     */
    public static LoadBalanceM getMsgApiUrl() {
        return msgApiUrl;
    }

    public static void setMsgApiUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        msgApiUrl = new LoadBalanceM(url.split(","));
    }

    /**
     * 日志服务地址
     */
    public static LoadBalanceM getLogApiUrl() {
        return logApiUrl;
    }

    public static void setLogApiUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        logApiUrl = new LoadBalanceM(url.split(","));
    }
}
