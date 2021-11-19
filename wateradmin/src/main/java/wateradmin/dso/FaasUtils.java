package wateradmin.dso;

import org.noear.water.WW;
import org.noear.water.WaterClient;
import org.noear.water.utils.EncryptUtils;
import org.noear.water.utils.TextUtils;
import wateradmin.Config;
import wateradmin.dso.db.DbLuffyApi;

public class FaasUtils {
    public static void trySubscribe(int file_id, String label, String path, boolean is_disabled) throws Exception {
        if (label.startsWith("@") && TextUtils.isEmpty(path) == false) {
            String topic = label.substring(1);
            //尝试退订
            if (file_id > 0) {
                String path_old = DbLuffyApi.getFile(file_id).path;
                if (path.equals(path_old) == false) {
                    //如果地址变了，退订
                    String subscriber_key_old = EncryptUtils.md5(path_old);
                    WaterClient.Message.unSubscribeTopic(null, subscriber_key_old, new String[]{topic});
                }
            }

            //订阅
            String receiver_url = "@" + WW.waterfaas + path;
            String subscriber_key = EncryptUtils.md5(path);

            if (is_disabled) {
                WaterClient.Message.unSubscribeTopic(null, subscriber_key, new String[]{topic});
            } else {
                WaterClient.Message.subscribeTopic(null, subscriber_key, receiver_url,
                        Config.waterfaas_secretKey, "", 0, false, new String[]{topic});
            }
        }
    }
}
