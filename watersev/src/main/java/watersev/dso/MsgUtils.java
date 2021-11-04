package watersev.dso;

import org.noear.solon.core.LoadBalance;

/**
 * @author noear 2021/11/4 created
 */
public class MsgUtils {
    /**
     * 解析接收地址
     * */
    public static String getReceiveUrl2(String receive_url) throws IllegalArgumentException {
        if (receive_url.startsWith("@")) {
            //说明是集群订阅模式
            int index = receive_url.indexOf("/");
            String service;
            String receive_url2;
            if (index < 0) {
                service = receive_url.substring(1);
                receive_url2 = LoadBalance.get(service).getServer() + "/msg/receive";
            } else {
                service = receive_url.substring(1, index);
                String path = receive_url.substring(index);
                receive_url2 = LoadBalance.get(service).getServer() + path;
            }

            if (receive_url2.indexOf("://") < 0) {
                throw new IllegalArgumentException("Unable to get the appropriate cluster node: @" + service);
            } else {
                return receive_url2;
            }
        } else {
            return receive_url;
        }
    }
}
