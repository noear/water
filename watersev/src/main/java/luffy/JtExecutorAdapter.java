package luffy;


import org.noear.luffy.executor.IJtConfigAdapter;
import org.noear.luffy.executor.IJtExecutorAdapter;
import org.noear.luffy.model.AFileModel;
import org.noear.solon.core.handle.Context;
import org.noear.solon.logging.utils.TagsMDC;
import org.noear.water.WaterClient;
import org.noear.water.model.LogLevel;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.LocalUtils;
import org.noear.water.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watersev.dso.AFileUtil;

import java.util.*;

/**
 * 执行工厂适配器
 * */

public class JtExecutorAdapter implements IJtExecutorAdapter, IJtConfigAdapter {
    static Logger log = LoggerFactory.getLogger("water_log_faas");

    private String _defaultExecutor = "freemarker";
    private String _defLogTag = "_plan";

    private final String water_log_faas = "water_log_faas"; //logger name
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

        WaterClient.Log.append(water_log_faas, LogLevel.DEBUG, data);
    }

    @Override
    public void logError(AFileModel file, String msg, Throwable err) {

        TagsMDC.tag0(_defLogTag);
        TagsMDC.tag1(file.tag);
        TagsMDC.tag2(file.path);

        log.error("{}\r\n{}", msg, err);
    }

    @Override
    public AFileModel fileGet(String path) throws Exception {
        return AFileUtil.get(path);
    }

    @Override
    public List<AFileModel> fileFind(String tag, String label, boolean isCache) throws Exception {
        return Collections.emptyList();
    }

    private String _nodeId;

    @Override
    public String nodeId() {
        if (_nodeId == null) {
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
        if (TextUtils.isEmpty(name)) {
            return def;
        }

        ConfigM tmp = null;
        if (name.indexOf("/") < 0) {
            tmp = WaterClient.Config.get(water_paas, name);
        } else {
            tmp = WaterClient.Config.getByTagKey(name);
        }


        if (tmp == null) {
            return def;
        }

        if (tmp.value == null) {
            return def;
        } else {
            return tmp.value;
        }
    }

    @Override
    public boolean cfgSet(String name, String value) throws Exception {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (name.indexOf("/") < 0) {
            WaterClient.Config.set(water_paas, name, value);
        } else {
            String[] ss = name.split("/");

            WaterClient.Config.set(ss[0], ss[1], value);
        }
        return true;
    }

    @Override
    public Map cfgMap(String name) throws Exception {
        ConfigM cfg = WaterClient.Config.get(water_paas, name);
        Map<String, Object> tmp = new LinkedHashMap<>();

        if (cfg != null) {
            tmp.put("value", cfg.value);
            tmp.put("name", name);
            tmp.put("tag", water_paas);
            tmp.put("update_fulltime", new Date(cfg.lastModified));
        }

        return tmp;
    }
}
