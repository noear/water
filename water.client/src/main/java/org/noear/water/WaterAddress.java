package org.noear.water;

/**
 * Water 内部服务地址
 *
 * @author noear
 * @since 2.0
 */
public class WaterAddress {
    private static WaterAddress waterAddress = new WaterAddress();
    public static WaterAddress getInstance(){
        return waterAddress;
    }

    private String configApiUrl;
    private String messageApiUrl;
    private String logApiUrl;
    private String registryApiUrl;
    private String trackApiUrl;
    private String noticeApiUrl;
    private String whitelistApiUrl;

    public WaterAddress(){
        configApiUrl = WaterSetting.water_api_url();
        messageApiUrl = WaterSetting.water_api_url();
        logApiUrl = WaterSetting.water_api_url();
        registryApiUrl = WaterSetting.water_api_url();
        trackApiUrl = WaterSetting.water_api_url();
        noticeApiUrl = WaterSetting.water_api_url();
        whitelistApiUrl = WaterSetting.water_api_url();
    }

    public String getConfigApiUrl() {
        return configApiUrl;
    }

    public void setConfigApiUrl(String configApiUrl) {
        this.configApiUrl = configApiUrl;
    }

    public String getMessageApiUrl() {
        return messageApiUrl;
    }

    public void setMessageApiUrl(String messageApiUrl) {
        this.messageApiUrl = messageApiUrl;
    }

    public String getLogApiUrl() {
        return logApiUrl;
    }

    public void setLogApiUrl(String logApiUrl) {
        this.logApiUrl = logApiUrl;
    }

    public String getRegistryApiUrl() {
        return registryApiUrl;
    }

    public void setRegistryApiUrl(String registryApiUrl) {
        this.registryApiUrl = registryApiUrl;
    }

    public String getTrackApiUrl() {
        return trackApiUrl;
    }

    public void setTrackApiUrl(String trackApiUrl) {
        this.trackApiUrl = trackApiUrl;
    }

    public String getNoticeApiUrl() {
        return noticeApiUrl;
    }

    public void setNoticeApiUrl(String noticeApiUrl) {
        this.noticeApiUrl = noticeApiUrl;
    }

    public String getWhitelistApiUrl() {
        return whitelistApiUrl;
    }

    public void setWhitelistApiUrl(String whitelistApiUrl) {
        this.whitelistApiUrl = whitelistApiUrl;
    }
}
