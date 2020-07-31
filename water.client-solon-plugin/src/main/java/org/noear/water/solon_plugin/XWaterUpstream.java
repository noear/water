package org.noear.water.solon_plugin;

import org.noear.solon.XApp;
import org.noear.solonclient.XUpstream;
import org.noear.solonclient.XProxy;
import org.noear.solonclient.annotation.XClient;
import org.noear.water.WaterClient;
import org.noear.water.WW;
import org.noear.water.dso.WaterUpstream;
import org.noear.water.model.DiscoverM;
import org.noear.water.model.DiscoverTargetM;
import org.noear.water.utils.HttpUtils;
import org.noear.water.utils.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负载器::Water Upstream （不能引用  XWaterAdapter）
 * */
public class XWaterUpstream implements WaterUpstream, XUpstream {
    private final String TAG_SERVER = "{server}";


    /**
     * 服务名
     * */
    private String _service;
    /**
     * 配置
     * */
    private DiscoverM _cfg;
    /**
     * 轮询值
     * */
    private int _polling_val = 0;
    /**
     * 节点列表
     * */
    protected List<String> _nodes = new ArrayList<>();
    /**
     * 节点数量
     * */
    private int _nodes_count;

    /**
     * 启用代理
     * */
    private boolean _enable_agent;

    /**
     * 备份服务节点
     * */
    private String _backup_server;


    protected final static Map<String, XWaterUpstream> _map = new ConcurrentHashMap<>();

    private XWaterUpstream(String service) {
        _service = service;
    }

    /**
     * 获取一个负载器
     * */
    public static XWaterUpstream get(String service) {
        XWaterUpstream tmp = _map.get(service);
        if (tmp == null) {
            synchronized (service.intern()) { //::与获取形成互锁
                tmp = _map.get(service);
                if (tmp == null) {
                    tmp = new XWaterUpstream(service).loadDo(false);
                    _map.put(service, tmp);
                }
            }
        }

        return tmp;
    }

    protected static XWaterUpstream getOnly(String service) {
        return _map.get(service);
    }



    /**
     * 重新加载负载配置
     */
    public XWaterUpstream reload() {
        return loadDo(true);
    }

    private XWaterUpstream loadDo(boolean lock) {

        DiscoverM cfg = WaterClient.Registry.discover(_service, WaterClient.localService(), WaterClient.localHost());

        if(lock){
            synchronized (_service.intern()) { //::与获取形成互锁
                loadDo0(cfg);
            }
        }else{
            loadDo0(cfg);
        }

        return this;
    }

    private void loadDo0(DiscoverM cfg) {
        //
        //使用前，要锁一下
        //
        if (cfg == null || TextUtils.isEmpty(cfg.policy)) {
            if(TextUtils.isNotEmpty(_backup_server)){
                //
                // 如果有默认的，则清空
                //
                _enable_agent = false;
                _nodes_count = 0;
                _nodes.clear();
            }
            return;
        }

        //检查model.url 是否可用
        if (cfg.url != null) {
            if (cfg.url.indexOf("://") > 0) {
                _cfg = cfg;//不能删
                _enable_agent = true;
            }
        } else {
            cfg.url = "";
        }

        _cfg = cfg;

        //构建可用服务地址 //支持轮询和带权重的轮询
        String sev_url;
        int sev_wgt;


        List<String> _nodes2 = new ArrayList<>();
        for (DiscoverTargetM m : _cfg.list) {
            sev_wgt = m.weight;
            sev_url = m.protocol + "://" + m.address;

            if (_cfg.url.contains(TAG_SERVER)) {
                sev_url = _cfg.url.replace(TAG_SERVER, sev_url);
            }

            while (sev_wgt > 0) {
                _nodes2.add(sev_url);
                sev_wgt--;
            }
        }

        //记录可用服务数
        //
        if(_nodes2.size() < _nodes_count){
            //旧的多，则先更新数量
            _nodes_count = _nodes.size();
            _nodes = _nodes2;
        }else{
            //新的多，则先更新节点
            _nodes = _nodes2;
            _nodes_count = _nodes.size();
        }

    }

    /**
     * 获取一个轮询节点
     * */
    public String get() {
        //1.如果有代理，则使用代理
        if (_enable_agent) {
            return _cfg.url;
        }

        //2.如果没有代理
        if (_nodes_count == 0) {
            //2.1.如果没有节点，则使用本地代理
            return _backup_server; //可能是null
        } else {
            //2.2.如果有节点；则使用节点
            if (_polling_val == 9999999) {
                _polling_val = 0;
            }

            _polling_val++;
            int idx = _polling_val % _nodes_count;


            return _nodes.get(idx);
        }
    }

    /**
     * 服务名
     */
    public String name() {
        return _service;
    }

    /**
     * 代理
     * */
    public String agent() {
        if (_cfg == null || _enable_agent == false) {
            return null;
        } else {
            return _cfg.url;
        }
    }

    /**
     * 负载策略
     */
    public String policy() {
        if (_cfg == null) {
            return null;
        } else {
            return _cfg.policy;
        }
    }

    /**
     * 服务节点
     */
    public List<String> nodes() {
        return Collections.unmodifiableList(_nodes);
    }

    @Override
    public String getServer(String name) {
        return get();
    }

    //
    // for http client
    //

    @Override
    public void setBackup(String server){
        _backup_server = server;
    }

    @Override
    public HttpUtils xcall(String path){
        return HttpUtils.http(get() + path)
                .headerAdd(WW.http_header_from, WaterClient.localServiceHost());

    }

    //
    // for rpc client
    //

    public static <T> T xclient(Class<?> clz) {
        XClient c_meta = clz.getAnnotation(XClient.class);

        if (c_meta == null) {
            throw new RuntimeException("No xclient annotation");
        }

        String c_sev = c_meta.value();
        if (TextUtils.isEmpty(c_sev)) {
            throw new RuntimeException("XClient no name");
        }

        //支持 rockrpc:/rpc 模式
        if (c_sev.indexOf(":") > 0) {
            c_sev = c_sev.split(":")[0];
        }

        XUpstream upstream = null;
        if (XApp.cfg().isDebugMode()) {
            //增加debug模式支持
            String url = System.getProperty("water.remoting-debug." + c_sev);
            if (url != null) {
                upstream = (s) -> url;
            }
        }

        if (upstream == null) {
            upstream = XWaterUpstream.get(c_sev);
        }

        return xclient(clz, upstream);
    }

    public static <T> T xclient(Class<?> clz, XUpstream upstream) {
        return new XProxy()
                .headerAdd(WW.http_header_from, WaterClient.localServiceHost())
                .upstream(upstream)
                .create(clz);
    }
}
