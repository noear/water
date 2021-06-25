package watersev.utils.cron;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author noear 2021/6/25 created
 */
public class CronUtil {
    private static Map<String, CronExpression> cacheMap = new LinkedHashMap<>();

    public static CronExpression get(String expression) throws ParseException {
        CronExpression tmp = cacheMap.get(expression);
        if (tmp == null) {
            synchronized (expression.intern()) {
                tmp = cacheMap.get(expression);
                if (tmp == null) {
                    tmp = new CronExpression(expression);
                    cacheMap.put(expression, tmp);
                }
            }
        }

        return tmp;
    }
}
