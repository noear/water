package org.noear.water.utils;

import org.apache.commons.lang3.CharEncoding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtils {
    /**
     * 压缩GZip
     *
     * @return String
     */
    public static String gZip(String input) throws IOException {
        byte[] bytes = null;
        GZIPOutputStream gzip = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(bos);
            gzip.write(input.getBytes(CharEncoding.UTF_8));
            gzip.finish();
            gzip.close();
            bytes = bos.toByteArray();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (gzip != null) {
                gzip.close();
            }

            if (bos != null) {
                bos.close();
            }
        }

        return Base64Utils.encodeByte(bytes);
    }

    /**
     * 解压GZip
     *
     * @return String
     */
    public static String unGZip(String input) throws IOException {
        byte[] bytes;
        String out = input;
        GZIPInputStream gzip = null;
        ByteArrayInputStream bis;
        ByteArrayOutputStream bos = null;
        try {
            bis = new ByteArrayInputStream(Base64Utils.decodeByte(input));
            gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
            int num;
            bos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                bos.write(buf, 0, num);
            }
            bytes = bos.toByteArray();

            out = new String(bytes, CharEncoding.UTF_8);

            gzip.close();
            bis.close();
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (gzip != null) {
                gzip.close();
            }

            if (bos != null) {
                bos.close();
            }
        }

        return out;
    }
}
