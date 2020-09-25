package waterapi.controller;

public class UapiCodes {
    /**
     * 失败，未知错误
     */
    public static final UapiCode CODE_0 = new UapiCode(0);
    /**
     * 成功
     */
    public static final UapiCode CODE_1 = new UapiCode(1);

    /**
     * 请求的接口不存在或不再支持
     */
    public static final UapiCode CODE_11 = new UapiCode(11);
    /**
     * 请求的签名校验失败
     */
    public static final UapiCode CODE_12 = new UapiCode(12);

    /**
     * 请求的参数缺少或有错误
     */
    public static final UapiCode CODE_13(String names) {
        return new UapiCode(13, names);
    }

    /**
     * 请求的不符合规范
     */
    public static final UapiCode CODE_14 = new UapiCode(14);

    /**
     * 请求太频繁了
     */
    public static final UapiCode CODE_15 = new UapiCode(15);
    /**
     * 请求不在白名单
     */
    public static final UapiCode CODE_16 = new UapiCode(16);

    public static final String getDescription(UapiCode error) {
        switch (error.getCode()) {
            case 1:
                return "Succeed";
            case 11:
                return "The api not exist";
            case 12:
                return "The signature error";
            case 13:
                return "Parameter missing or error" + (error.getDescription() == null ? "" : "(" + error.getDescription() + ")");
            case 14:
                return "The request is not up to par";
            case 15:
                return "Too many requests";
            case 16:
                return "The request is not in the whitelist";
            default:
                return "Unknown error!";
        }
    }
}
