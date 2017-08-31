package watersev.utils;

import watersev.dao.LogUtil;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by yuety on 2017/7/11.
 */
public class JsUtil {
    static ScriptEngine engine = null;
    static {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

        engine = scriptEngineManager.getEngineByName("nashorn");
    }

    public synchronized static void loadJs(String code){
        try {
            engine.eval(code);
        }catch (Exception ex){
            ex.printStackTrace();
            LogUtil.error("JsUtil",ex);
        }
    }

    public static Object evalJs(String jscode){
        try {
            return engine.eval(jscode);
        }catch (Exception ex){
            ex.printStackTrace();
            LogUtil.error("JsUtil",ex);
            return null;
        }
    }

    public static Object callJs(String fun, Object... args) {
        Invocable invocable = (Invocable) engine;

        try {
            return (String) invocable.invokeFunction(fun, args);
        }catch (Exception ex){
            ex.printStackTrace();
            LogUtil.error("JsUtil",ex);
            return null;
        }
    }
}
