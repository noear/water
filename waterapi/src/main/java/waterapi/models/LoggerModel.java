package waterapi.models;

import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

public class LoggerModel implements IBinder {
    public  String tag;
    public  String logger;
    public  String source;

    @Override
    public void bind(GetHandlerEx s) {
        tag = s.get("tag").value("");
        logger = s.get("logger").value("");
        source = s.get("source").value("");
    }

    @Override
    public IBinder clone() {
        return new LoggerModel();
    }
}
