package org.noear.water.utils;

import org.noear.snack.ONode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsondUtils {
    private static final String key = "j$6gxA^KJgBiOgco";

    public static String encode(String table, List data) throws IOException {
        JsondEntity entity = new JsondEntity();
        entity.table = table;
        entity.data = ONode.loadObj(data);

        //转码
        String json = ONode.stringify(entity);

        //压缩
        String gzip = GzipUtils.compressToString(json);

        //加密
        return EncryptUtils.aesEncrypt(gzip, key);
    }

    public static JsondEntity decode(String jsonD) throws IOException {
        //解密
        String gzip = EncryptUtils.aesDecrypt(jsonD, key);

        //解压
        String json = GzipUtils.uncompressToString(gzip.getBytes(StandardCharsets.UTF_8));

        //转码
        return ONode.deserialize(json, JsondEntity.class);
    }
}
