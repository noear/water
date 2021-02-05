package org.noear.water.protocol;

import org.noear.water.utils.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface IdBuilder {
    long getId(String tag);

    default long getMsgId() {
        return getId("msg_id");
    }

    default long getLogId(String logger) {
        return getId("log_id_" + logger);
    }

    static boolean isNumeric(String str) {
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
}
