package waterapp.controller.cmds;

import org.noear.snack.ONode;
import waterapp.utils.TextUtils;
import org.noear.solon.core.XContext;
import waterapp.Config;
import waterapp.dso.LogUtils;
import waterapp.dso.TraceUtils;
import waterapp.dso.db.DbWaterCfgApi;
import waterapp.utils.IPUtil;
import waterapp.utils.Timecount;

import java.sql.SQLException;

/**
 * Created by noear on 2017/7/19.
 */
public abstract class CMDBase {
    protected XContext context;
    protected ONode data;

    protected boolean isTrack(){return true;}
    protected boolean isLogging(){return true;}

    protected String get(String key) {
        return context.param(key);
    }

    protected String get(String key, String def) {
        return context.param(key, def);
    }

    protected int getInt(String key){
        return getInt(key, 0);
    }

    protected int getInt(String key, int def){
        String val = get(key);
        if(TextUtils.isEmpty(val)) {
            return def;
        }
        else {
            return Integer.parseInt(val);
        }
    }

    protected long getlong(String key){
        String val = get(key);
        if(TextUtils.isEmpty(val)) {
            return 0;
        }
        else {
            return Long.parseLong(val);
        }
    }

    public boolean isOutput(){
        return true;
    }

    public void exec(XContext context) {

        this.context = context;
        this.data = new ONode();

        Timecount timecount = new Timecount().start();

        try {
            if (do1_check_ip()) {
                if(isLogging()) {
                    LogUtils.info("", context);
                }

                do2_exec();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            LogUtils.error(context, ex);

            data.set("code", "0");
            data.set("msg", ex.getMessage());
        }

        long timespan = timecount.stop().milliseconds();

        if(isTrack()) {
            TraceUtils.track(Config.water_service_name, "cmd", context.path(), timespan);
        }

        if (isOutput()) {
            do3_ouput();
        }
    }

    protected abstract void cmd_exec() throws Exception;

    private final boolean do1_check_ip() throws SQLException{
        String ip = IPUtil.getIP(context);

        data.set("_ip",ip);

        if (DbWaterCfgApi.isWhitelist(ip) == false) {
            data.set("code", 2);
            data.set("msg", ip + ",not is whitelist!");
            return false;
        }else{
            return true;
        }
    }

    private final  void do2_exec() throws Exception{
        cmd_exec();
    }

    private final void do3_ouput() {
        try {
            context.charset("UTF-8");//设置响应的编码类型为UTF-8
            context.contentType("text/json;charset=UTF-8");

            context.output(data.toJson());
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtils.error(context, ex);
        }
    }

    //==========
    protected final boolean checkParamsIsOk(String... strs) {
        for (String str : strs) {
            if (TextUtils.isEmpty(str)) {

                data.set("code", 3);
                data.set("msg", "not parameter");

                return false;
            }
        }

        return true;
    }
}
