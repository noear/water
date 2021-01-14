package org.noear.water.utils;

import org.noear.water.utils.ext.Act0Ex;
import org.noear.water.utils.ext.Fun0Ex;

public class CallUtils {
    public static void run(Act0Ex fun) {
        try {
            fun.run();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void runTry(Act0Ex fun) {
        try {
            fun.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static <T> T call(Fun0Ex<T> fun) {
        try {
            return fun.run();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T callTry(Fun0Ex<T> fun) {
        try {
            return fun.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
