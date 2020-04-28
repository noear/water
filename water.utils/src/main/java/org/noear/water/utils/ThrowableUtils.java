package org.noear.water.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableUtils {
    public static String getString(Throwable ex){
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));

        return sw.toString();
    }
}
