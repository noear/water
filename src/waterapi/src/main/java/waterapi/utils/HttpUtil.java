package waterapi.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;

/**
 * Created by Mazexal on 2017/4/25.
 */
public class HttpUtil {

    public static String getIP(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("Proxy-Client-IP");

        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getHeader("WL-Proxy-Client-IP");

        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

            ip = request.getRemoteAddr();

        }

        return ip;
    }

    public static String encodeBase46(String str){
        byte[] btys = str.getBytes(Charset.forName("UTF-8"));
        return new String(Base64.getEncoder().encode(btys));
    }

    public static String decodeBase46(String str){
        byte[] btys = str.getBytes(Charset.forName("UTF-8"));
        return new String(Base64.getDecoder().decode(btys));
    }
}
