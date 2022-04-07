package org.noear.water.dso;

import org.noear.snack.ONode;
import org.noear.water.WaterAddress;
import org.noear.water.model.KeyM;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author noear 2022/4/7 created
 */
public class KeyApi {
    protected final ApiCaller apiCaller;

    public KeyApi() {
        apiCaller = new ApiCaller(WaterAddress.getConfigApiUrl());
    }

    Map<String, KeyM> keyMap = Collections.synchronizedMap(new HashMap());

    /**
     * 获取密钥
     * */
    public KeyM getKey(String accessKey) throws IOException {
        KeyM keyM = keyMap.get(accessKey);

        if (keyM == null) {
            synchronized (accessKey.intern()) {
                keyM = keyMap.get(accessKey);

                if (keyM == null) {
                    keyM = loadKey(accessKey);
                }
                keyMap.put(accessKey, keyM);
            }
        }

        return keyM;
    }

    protected KeyM loadKey(String accessKey) throws IOException {

        String json = apiCaller.http("/key/get/")
                .data("accessKey", accessKey)
                .post();

        ONode oNode = ONode.loadStr(json);

        KeyM keyM;
        int code = oNode.get("code").getInt();
        if (code == 200) {
            keyM = oNode.get("data").toObject(KeyM.class);
        } else {
            keyM = new KeyM();
        }

        return keyM;
    }
}
