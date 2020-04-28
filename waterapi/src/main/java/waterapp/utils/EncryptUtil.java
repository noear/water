package waterapp.utils;


import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by org.noear.on 2017/7/28.
 */
public class EncryptUtil {

    public static String md5(String str){
        //定义一个字节数组
        byte[] secretBytes = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(str.getBytes("UTF-8"));
            //获得加密后的数据
            secretBytes = md.digest();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //将加密后的数据转换为16进制数字
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
}
