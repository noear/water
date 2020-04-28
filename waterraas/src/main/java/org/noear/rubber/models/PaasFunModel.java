package org.noear.rubber.models;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

public class PaasFunModel implements IBinder {
    public String tag;
    public String fun_name;
    public String args;
    public String code;

    @Override
    public void bind(GetHandlerEx s) {
        tag = s.get("tag").value("");
        fun_name = s.get("fun_name").value("");
        args = s.get("args").value("");
        code = s.get("code").value("");
    }

    @Override
    public IBinder clone() {
        return new PaasFunModel();
    }
}
