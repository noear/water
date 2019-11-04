package org.noear.water.client.dso;

import org.noear.water.client.model.MessageModel;
import org.noear.weed.utils.EncryptUtils;

public class MessageUtil {
    /**
     * 检查消息
     */
    public static boolean checkMessage(MessageModel msg, String receive_secret) {

        if (msg.id < 1) {
            return false;
        }

        StringBuilder sb = new StringBuilder(200);
        sb.append(msg.id).append("#");
        sb.append(msg.key).append("#");
        sb.append(msg.topic).append("#");
        sb.append(msg.message).append("#");
        sb.append(receive_secret);

        String sgin_slf = EncryptUtils.md5(sb.toString());

        return sgin_slf.equals(msg.sgin);
    }
}
