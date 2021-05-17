package org.noear.water.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author noear 2021/5/17 created
 */

public class ConvertUtil {
    private static final SimpleDateFormat DATE_DEF_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");


    /**
     * 转换 properties 的值
     *
     * @param type 目标类型
     * @param val 属性值
     * */
    public static Object to(Class<?> type, String val) throws ClassCastException{
        if (String.class == (type)) {
            return val;
        }

        if (val.length() == 0) {
            return null;
        }

        Object rst = tryTo(type, val);

        if (rst != null) {
            return rst;
        }

        if (Date.class == (type)) {
            try {
                return DATE_DEF_FORMAT.parse(val);
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }


        throw new ClassCastException("不支持类型:" + type.getName());
    }

    /**
     * 转换 string 值
     *
     * @param type 目标类型
     * @param val 值
     * */
    public static Object tryTo(Class<?> type, String val) {
        if (Short.class == type || type == Short.TYPE) {
            return Short.parseShort(val);
        }

        if (Integer.class == type || type == Integer.TYPE) {
            return Integer.parseInt(val);
        }

        if (Long.class == type || type == Long.TYPE) {
            return Long.parseLong(val);
        }

        if (Double.class == type || type == Double.TYPE) {
            return Double.parseDouble(val);
        }

        if (Float.class == type || type == Float.TYPE) {
            return Float.parseFloat(val);
        }

        if (Boolean.class == type || type == Boolean.TYPE) {
            return Boolean.parseBoolean(val);
        }

        if (LocalDate.class == type) {
            //as "2007-12-03", not null
            return LocalDate.parse(val);
        }

        if (LocalTime.class == type) {
            //as "10:15:30", not null
            return LocalTime.parse(val);
        }

        if (LocalDateTime.class == type) {
            //as "2007-12-03T10:15:30", not null
            return LocalDateTime.parse(val);
        }

        if (BigDecimal.class == type) {
            return new BigDecimal(val);
        }

        if (BigInteger.class == type) {
            return new BigInteger(val);
        }

        if (type.isEnum()) {
            return Enum.valueOf((Class<Enum>) type, val);
        }

        return null;
    }

    /**
     * 检测类型是否相同
     *
     * @param s 源类型
     * @param t 目标类型
     * */
    private static boolean is(Class<?> s, Class<?> t){
        return s.isAssignableFrom(t);
    }

}