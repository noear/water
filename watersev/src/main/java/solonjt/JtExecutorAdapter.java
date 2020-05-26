package solonjt;


import org.noear.solonjt.executor.IJtConfigAdapter;
import org.noear.solonjt.executor.IJtExecutorAdapter;
import org.noear.solonjt.model.AFileModel;
import org.noear.water.WaterClient;
import org.noear.water.log.Level;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.LocalUtils;
import waterapp.dso.AFileUtil;

import java.util.Map;

/**
 * 执行工厂适配器
 * */
public class JtExecutorAdapter implements IJtExecutorAdapter, IJtConfigAdapter {

    private String _defaultExecutor = "freemarker";

    public JtExecutorAdapter() {
    }

    @Override
    public void log(AFileModel file, Map<String, Object> data) {
        if (data.containsKey("tag2") == false && file != null) {
            data.put("tag2", file.path);
        }

        WaterClient.Log.append("water_log_paas", Level.DEBUG, data);
    }

    @Override
    public void logError(AFileModel file, String msg, Throwable err) {
        WaterClient.Log.append("water_log_paas", Level.ERROR, "_file", file.tag, file.path, "", "", msg);
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
        if(name == null || name.indexOf("/")<0){
            return def;
        }

        ConfigM tmp = WaterClient.Config.getByTagKey(name);

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
        if (name == null || name.indexOf("/") < 0) {
            return false;
        }

        String[] ss = name.split("/");

        WaterClient.Config.set(ss[0], ss[1], value);
        return true;
    }
}
