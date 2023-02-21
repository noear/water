package luffy;

import org.noear.esearchx.EsContext;
import org.noear.luffy.dso.JtFun;
import org.noear.luffy.dso.JtUtil;
import org.noear.mongox.MgContext;
import org.noear.redisx.RedisClient;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Note;
import org.noear.water.WaterClient;
import org.noear.water.WaterProxy;
import org.noear.water.model.ConfigM;
import org.noear.wood.DbContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author noear 2023/2/21 created
 */
public class WaterImpl {
    public static final WaterImpl g = new WaterImpl();

    @Note("获取配置")
    public ConfigM cfg(String tagKey) {
        return WaterClient.Config.getByTagKey(tagKey);
    }

    @Note("获取配置，并转为db对象")
    public DbContext db(String tagKey) {
        return cfg(tagKey).getDb();
    }

    @Note("获取配置，并转为rd对象")
    public RedisClient rd(String tagKey) {
        return rd(tagKey, 0);
    }

    @Note("获取配置，并转为rd对象")
    public RedisClient rd(String tagKey, int i) {
        return cfg(tagKey).getRd(i);
    }

    @Note("获取配置，并转为mg对象")
    public MgContext mg(String tagKey, String c) {
        return cfg(tagKey).getMg(c);
    }

    @Note("获取配置，并转为es对象")
    public EsContext es(String tagKey) {
        return cfg(tagKey).getEs();
    }


    @Note("调用函数")
    public String call(String service, String fun) throws Exception {
        return call(service, fun,null);
    }

    @Note("调用函数")
    public String call(String service, String fun, Map args) throws Exception {
        if (args == null) {
            args = new HashMap();
        }

        return WaterProxy.call(service, fun, args);
    }

    @Note("调用文件")
    public Object callFile(String path) throws Exception {
        return callFile(path, null);
    }
    @Note("调用文件")
    public Object callFile(String path, Map attrs) throws Exception {
        if (attrs == null) {
            attrs = new HashMap();
        }

        return JtFun.g.callFile(path, attrs);
    }

    @Note("调用 FaaS")
    public String faas(String path) throws Exception {
        return faas(path, null);
    }

    @Note("调用 FaaS")
    public String faas(String path, Map args) throws Exception {
        if (args == null) {
            args = new HashMap();
        }

        return WaterProxy.faas(path, args);
    }

    @Note("调用 FaaS 并转为对象")
    public Object faasAsJson(String path) throws Exception {
        return faasAsJson(path, null);
    }
    @Note("调用 FaaS 并转为对象")
    public Object faasAsJson(String path, Map args) throws Exception {
        return ONode.deserialize(faas(path, args));
    }

    @Note("调用 RaaS")
    @Deprecated
    public String raas(String path) throws Exception {
       return raas(path, null);
    }
    @Note("调用 RaaS")
    @Deprecated
    public String raas(String path, Map args) throws Exception {
        if (args == null) {
            args = new HashMap<>();
        }

        return WaterProxy.raas(path, args);
    }

    public void logTrace(String logger, Map data) {
        WaterProxy.logTrace(logger, data);
    }

    public void logDebug(String logger, Map data) {
        WaterProxy.logDebug(logger, data);
    }

    public void logInfo(String logger, Map data) {
        WaterProxy.logInfo(logger, data);
    }

    public void logWarn(String logger, Map data) {
        WaterProxy.logWarn(logger, data);
    }

    public void logError(String logger, Map data) {
        WaterProxy.logError(logger, data);
    }

    ////////////////////

    @Note("运行任务")
    public String job(String service, String job) throws Exception {
        return job(service, job, null);
    }

    @Note("运行任务")
    public String job(String service, String job, Map args) throws Exception {
        if (args == null) {
            args = new HashMap();

        }
        String rst = WaterProxy.runJob(service, job, args);

        JtUtil.g.log("Job return: " + rst);

        if ("OK".equals(rst) == false) {
            throw new IllegalStateException("Job return: " + rst);
        } else {
            return rst;
        }
    }

    @Note("获取状态")
    public String getStatus(String addrees) throws Exception {
        return WaterProxy.runStatus(addrees);
    }


    @Note("告警通知")
    public String heihei(String target, String msg) {
        return heihei(target, msg, null);
    }
    @Note("告警通知")
    public String heihei(String target, String msg, String sign) {
        return WaterClient.Notice.heihei(target, msg, sign);
    }

    @Note("更新缓存")
    public void updateCache(String tags) {
        WaterClient.Notice.updateCache(tags);
    }

    @Note("发送消息")
    public boolean sendMessage(String topic, String message) throws Exception {
        return WaterClient.Message.sendMessage("", topic, message);
    }
}
