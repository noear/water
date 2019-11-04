package org.noear.water.client.utils;

import org.noear.water.client.utils.ext.Act0Ex;
import org.noear.water.client.utils.ext.Fun0Ex;

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
