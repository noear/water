package org.noear.water.solon_plugin;


import org.noear.solon.XApp;
import org.noear.solonclient.HttpUpstream;
import org.noear.solonclient.XProxy;
import org.noear.solonclient.annotation.XClient;
import org.noear.water.WaterClient;
import org.noear.water.WW;
import org.noear.water.model.DiscoverM;
import org.noear.water.model.DiscoverTargetM;
import org.noear.water.utils.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负载器::Water Upstream （不能引用  XWaterAdapter）
 * */
public class XWaterUpstream implements HttpUpstream {
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
    private int _upstream_val = 0;
    /**
     * 节点数量
     * */
    private int _count;
    /**
     * 节点列表
     * */
    protected final List<String> _list = new ArrayList<>();
    /**
     * 使用url值
     * */
    private boolean _use_url;

    /**
     * 消费者
     * */
    protected static String _consumer;
    /**
     * 消费者地址
     * */
    protected static String _consumer_address;



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
                    tmp = new XWaterUpstream(service).reloadDo();
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
        synchronized (_service.intern()) { //::与获取形成互锁
            return reloadDo();
        }
    }

    private XWaterUpstream reloadDo() {
        if (_consumer == null) {
            _consumer = "";
        }

        if (_consumer_address == null) {
            _consumer_address = "";
        }

        DiscoverM mod = WaterClient.Registry.discover(_service, _consumer, _consumer_address);
        doLoad(mod);

        return this;
    }

    /**
     * 服务名
     */
    public String name() {
        return _service;
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
        return Collections.unmodifiableList(_list);
    }

    private void doLoad(DiscoverM cfg) {
        if (cfg == null || TextUtils.isEmpty(cfg.policy)) {
            return;
        }

        _cfg = cfg;

        //检查model.url 是否可用
        if (_cfg.url != null) {
            if (_cfg.url.indexOf("://") > 0) {
                _use_url = true;
            }
        } else {
            _cfg.url = "";
        }

        //构建可用服务地址 //支持轮询和带权重的轮询
        String sev_url;
        int sev_wgt;
        _list.clear();
        for (DiscoverTargetM m : _cfg.list) {
            sev_wgt = m.weight;
            sev_url = m.protocol + "://" + m.address;

            if (_cfg.url.contains(TAG_SERVER)) {
                sev_url = _cfg.url.replace(TAG_SERVER, sev_url);
            }

            while (sev_wgt > 0) {
                _list.add(sev_url);
                sev_wgt--;
            }
        }

        //记录可用服务数
        _count = _list.size();
    }

    /**
     * 获取一个轮询节点
     * */
    public String get() {
        return getDo();
    }

    private String getDo() {
        if (_use_url) {
            return _cfg.url;
        }

        if (_count == 0) {
            return null;
        }

        synchronized (_service.intern()) { //::与更新形成互锁
            if (_upstream_val == 9999999) {
                _upstream_val = 0;
            }

            _upstream_val++;
            int idx = _upstream_val % _count;

            return _list.get(idx);
        }
    }

    @Override
    public String getTarget(String name) {
        return get();
    }


    //
    //
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

        HttpUpstream upstream = null;
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

    public static <T> T xclient(Class<?> clz, HttpUpstream upstream) {
        if (XWaterUpstream._consumer == null) {
            XWaterUpstream._consumer = "";
        }

        if (XWaterUpstream._consumer_address == null) {
            XWaterUpstream._consumer_address = "";
        }

        return new XProxy()
                .headerAdd(WW.http_header_from, XWaterUpstream._consumer + "@" + XWaterUpstream._consumer_address)
                .upstream(upstream)
                .create(clz);
    }
}
