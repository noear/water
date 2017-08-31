package watersev.models.water;

import noear.weed.DbContext;
import noear.weed.GetHandlerEx;
import noear.weed.IBinder;

public class ConfigModel implements IBinder {
    public String tag;
    public String key;
    public String url;
    public String user;
    public String password;
    public String explain;

    @Override
    public void bind(GetHandlerEx s) {
        tag = s.get("tag").value("");
        key = s.get("key").value("");
        url = s.get("url").value("");
        user = s.get("user").value("");
        password = s.get("password").value("");
        explain = s.get("explain").value("");
    }

    @Override
    public IBinder clone() {
        return new ConfigModel();
    }


    public  DbContext getDb() {
        return new DbContext(key.replace("_r", "").replace("_rtm",""), url, user, password, "");
    }
}
