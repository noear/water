package wateradmin.dso;

import org.noear.water.WaterClient;
import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.TextUtils;
import wateradmin.Config;
import wateradmin.dso.db.DbPaaSApi;

public class PaasUtils {
    public static void trySubscribe(int file_id, String label, String path, boolean is_disabled) throws Exception {
        if (label.startsWith("@") && TextUtils.isEmpty(path) == false) {
            String topic = label.substring(1);
            //尝试退订
            if (file_id > 0) {
                String path_old = DbPaaSApi.getFile(file_id).path;
                if (path.equals(path_old) == false) {
                    //如果地址变了，退订
                    String subscriber_key_old = EncryptUtils.md5(path_old);
                    WaterClient.Message.unSubscribeTopic(subscriber_key_old, topic);
                }
            }

            //订阅
            String receiver_url = Config.paas_uri() + path;
            String subscriber_key = EncryptUtils.md5(path);

            if (is_disabled) {
                WaterClient.Message.unSubscribeTopic(subscriber_key, topic);
            } else {
                WaterClient.Message.subscribeTopic(subscriber_key, receiver_url,
                        Config.waterpaas_secretKey, "", 0, false, topic);
            }
        }
    }
}
