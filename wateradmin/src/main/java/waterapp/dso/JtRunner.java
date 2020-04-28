package waterapp.dso;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JtRunner {
    private static ScriptEngine _eng;

    public static Object eval(String script) {
        if (_eng == null) {
            System.setProperty("nashorn.args", "--language=es6");

            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            _eng = scriptEngineManager.getEngineByName("nashorn");
        }

        try {
            return _eng.eval(script);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Object eval(String code, Object def) {
        Object tmp = eval(code);
        if (tmp == null) {
            return def;
        } else {
            return tmp;
        }
    }
}
