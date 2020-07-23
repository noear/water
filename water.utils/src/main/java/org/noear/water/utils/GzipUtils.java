package org.noear.water.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtils {
    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";

    private static ByteArrayOutputStream compressDo(String str, String encoding){
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }

    public static byte[] compress(String str, String encoding) {
        if(TextUtils.isEmpty(str)){
            return null;
        }

        return compressDo(str, encoding).toByteArray();
    }

    public static byte[] compress(String str)  {
        if(TextUtils.isEmpty(str)){
            return null;
        }

        return compress(str, GZIP_ENCODE_UTF_8);
    }

    public static String compressToString(String str, String encoding) throws IOException{
        if(TextUtils.isEmpty(str)){
            return null;
        }

        return compressDo(str, encoding).toString(encoding);
    }

    public static String compressToString(String str) throws IOException {
        if(TextUtils.isEmpty(str)){
            return null;
        }

        return compressToString(str, GZIP_ENCODE_UTF_8);
    }


    private static ByteArrayOutputStream uncompressDo(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return out;
    }

    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        ByteArrayOutputStream tmp = uncompressDo(bytes);
        if (tmp == null) {
            return null;
        } else {
            return tmp.toByteArray();
        }
    }

    public static String uncompressToString(byte[] bytes, String encoding) throws IOException {
        if (bytes == null) {
            return null;
        }

        ByteArrayOutputStream tmp = uncompressDo(bytes);
        if (tmp == null) {
            return null;
        } else {
            return tmp.toString(encoding);
        }
    }

    public static String uncompressToString(byte[] bytes) throws IOException{
        if(bytes == null){
            return null;
        }

        return uncompressToString(bytes, GZIP_ENCODE_UTF_8);
    }
}
