package wateradmin.dso;

import org.noear.water.utils.IDUtils;
import org.noear.water.utils.RedisX;
import org.noear.water.utils.TextUtils;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IDUtil {

    public static String buildGuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getCodeByID(long id) {
        long code = id;
        long key = 999999999;

        code = code + key;
        code = code - (key / 100);
        code = code + (key / 10000);
        code = code - (key / 1000000);
        code = code + 1;

        return Long.toString(code, 36);
    }


    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static long getID(String tag)  {
        return buildID(tag, 1);
    }

    private static long buildID(String tag, long startIndex)  {
        return IDUtils.newID("ID", tag) + startIndex;
    }
}
