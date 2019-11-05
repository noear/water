package org.noear.water.tools;


import org.noear.water.tools.ext.Act0Ex;
import org.noear.water.tools.ext.Fun0Ex;

public class RunUtil {
    public static void runActEx(Act0Ex act0Ex) {
        try {
            act0Ex.run();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <R> R runFunEx(Fun0Ex<R> fun0Ex) {
        try {
            return fun0Ex.run();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
