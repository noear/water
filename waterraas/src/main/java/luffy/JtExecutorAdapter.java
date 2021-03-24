package luffy;


import org.noear.luffy.executor.IJtConfigAdapter;
import org.noear.luffy.executor.IJtExecutorAdapter;
import org.noear.luffy.model.AFileModel;
import org.noear.solon.core.handle.Context;
import org.noear.water.WaterClient;
import org.noear.water.log.Level;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.LocalUtils;
import org.noear.water.utils.TextUtils;
import waterraas.dao.AFileUtil;


import java.util.Map;

/**
 * 执行工厂适配器
 * */
public class JtExecutorAdapter implements IJtExecutorAdapter, IJtConfigAdapter {

    private String _defaultExecutor = "freemarker";
    private String _defLogTag = "_raas";

    private final String water_log_paas = "water_log_paas"; //logger name
    private final String water_paas = "water_paas"; //config tag name

    public JtExecutorAdapter() {
    }

    @Override
    public void log(AFileModel file, Map<String, Object> data) {
        if (file == null) {
            Context ctx = Context.current();

            if (ctx != null) {
                file = ctx.attr("file");
            }
        }

        if (data.containsKey("tag") == false) {
            data.put("tag", _defLogTag);
        }

        if (file != null) {
            if (data.containsKey("tag1") == false) {
                data.put("tag1", file.tag);
            }

            if (data.containsKey("tag2") == false) {
                data.put("tag2", file.path);
            }
        }

        WaterClient.Log.append(water_log_paas, Level.DEBUG, data);
    }

    @Override
    public void logError(AFileModel file, String msg, Throwable err) {
        WaterClient.Log.append(water_log_paas, Level.ERROR, _defLogTag, file.tag, file.path, "", "", msg);
    }

    @Override
    public AFileModel fileGet(String path) throws Exception {
        return AFileUtil.get(path);
    }

    private String _nodeId;
    @Override
    public String nodeId() {
        if(_nodeId == null) {
            _nodeId = LocalUtils.getLocalIp();
        }

        return _nodeId;
    }

    @Override
    public String defaultExecutor() {
        return _defaultExecutor;
    }

    public void defaultExecutorSet(String defaultExecutor) {
        _defaultExecutor = defaultExecutor;
    }

    @Override
    public String cfgGet(String name, String def) throws Exception {
        if(TextUtils.isEmpty(name)){
            return def;
        }

        ConfigM tmp = null;
        if(name.indexOf("/") < 0){
            tmp = WaterClient.Config.get(water_paas,name);
        }else {
            tmp = WaterClient.Config.getByTagKey(name);
        }


        if(tmp == null){
            return def;
        }

        if(tmp.value == null){
            return def;
        }else{
            return tmp.value;
        }
    }

    @Override
    public boolean cfgSet(String name, String value) throws Exception {
        if(TextUtils.isEmpty(name)){
            return false;
        }
        if(name.indexOf("/") < 0){
            WaterClient.Config.set(water_paas, name, value);
        }else {
            String[] ss = name.split("/");

            WaterClient.Config.set(ss[0], ss[1], value);
        }
        return true;
    }
}
