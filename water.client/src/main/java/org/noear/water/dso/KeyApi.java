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
    Map<Integer, KeyM> keyMap2 = Collections.synchronizedMap(new HashMap());

    /**
     * 获取密钥
     */
    public KeyM getKeyByAccessKey(String accessKey) throws IOException {
        KeyM keyM = keyMap.get(accessKey);

        if (keyM == null) {
            synchronized (accessKey.intern()) {
                keyM = keyMap.get(accessKey);

                if (keyM == null) {
                    keyM = loadDo(accessKey, 0);
                }

                keyMap2.put(keyM.getKeyId(), keyM);
                keyMap.put(accessKey, keyM);
            }
        }

        return keyM;
    }

    /**
     * 获取密钥
     */
    public KeyM getKeyById(Integer keyId) throws IOException {
        KeyM keyM = keyMap2.get(keyId);

        if (keyM == null) {
            synchronized (keyId) {
                keyM = keyMap.get(keyId);

                if (keyM == null) {
                    keyM = loadDo("", keyId);
                }

                keyMap2.put(keyId, keyM);
                keyMap.put(keyM.getAccessKey(), keyM);
            }
        }

        return keyM;
    }

    /**
     * 刷新密钥
     */
    public void refresh(String accessKey, int orKeyId) throws IOException {
        KeyM keyM = loadDo(accessKey, orKeyId);
        if (keyM.getKeyId() > 0) {
            keyMap2.put(keyM.getKeyId(), keyM);
            keyMap.put(keyM.getAccessKey(), keyM);
        }
    }

    protected KeyM loadDo(String accessKey, int orKeyId) throws IOException {

        String json = apiCaller.http("/key/get/")
                .data("accessKey", accessKey)
                .data("keyId", String.valueOf(orKeyId))
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
