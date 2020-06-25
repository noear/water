package org.noear.water.solon_plugin;

import org.noear.solon.core.XContext;
import org.noear.water.WaterClient;
import org.noear.water.WW;
import org.noear.water.model.MessageM;
import org.noear.weed.WeedConfig;
import org.noear.solon.XApp;
import org.noear.solon.XUtil;
import org.noear.solon.core.XPlugin;
import org.noear.weed.cache.ICacheServiceEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//
// Water for service project adapter
//
public abstract class XWaterAdapter extends XWaterAdapterBase implements XPlugin {
    protected static XWaterAdapter _global;

    public static XWaterAdapter global() {
        return _global;
    }


    private Map<String, XMessageHandler> _router;

    public Map<String, XMessageHandler> router() {
        return _router;
    }


    public String msg_receiver_url() {
        return null;
    }

    public XWaterAdapter() {
        super(XApp.cfg().argx(), XApp.global().port());
        _global = this;

        XUtil.loadClass("com.mysql.jdbc.Driver");
        XUtil.loadClass("com.mysql.cj.jdbc.Driver");
    }

    @Override
    public void start(XApp app) {
        app.all(service_check_path, this::handle);
        app.all(service_stop_path, this::handle);
        app.all(msg_receiver_path, this::handle);
    }

    @Override
    protected void onInit() {
        _router = new HashMap<>();

        registerService();

        messageListening(_router);

        messageSubscribe();

        initWeed();
    }

    //用于作行为记录
    public int user_puid() {
        if (XContext.current() != null) {
            String tmp = XContext.current().attr("user_puid", "0");
            return Integer.parseInt(tmp);
        } else {
            return 0;
        }
    }

    public String user_name() {
        if (XContext.current() != null) {
            return XContext.current().attr("user_name", null);
        } else {
            return null;
        }
    }

    protected void initWeed() {
        Class<?> clz = XUtil.loadClass(WW.clz_BcfClient);

        if (clz == null) {
            //api项目
            WeedConfig.onExecuteAft(cmd -> {
                WaterClient.Track.track(service_name(), cmd, 1000);
            });
        } else {
            //admin 项目
            WeedConfig.onExecuteAft((cmd) -> {
                System.out.println(cmd.text);

                if (cmd.isLog < 0) {
                    return;
                }

                if (user_name() == null) {
                    return;
                }

                XContext context = XContext.current();

                String sqlUp = cmd.text.toUpperCase();
                String chkUp = "User_Id=? AND Pass_Wd=? AND Is_Disabled=0".toUpperCase();

                if (cmd.timespan() > 2000 || cmd.isLog > 0 || sqlUp.indexOf("INSERT INTO ") >= 0 || sqlUp.indexOf("UPDATE ") >= 0 || sqlUp.indexOf("DELETE ") >= 0 || sqlUp.indexOf(chkUp) >= 0) {
                    WaterClient.Track.track(service_name(), cmd, context.userAgent(), context.path(), user_puid() + "." + user_name(), IPUtils.getIP(context));
                }
            });
        }
    }

    //支持手动加入监听(保持旧的兼容)
    public void messageListening(Map<String, XMessageHandler> map) {
    }

    ;

    @Override
    public void messageSubscribeHandler() {
        if (_router.size() == 0) {
            return;
        }

        //远程订阅时，不包括water主题(由本地订阅处理)
        List<String> _list = new ArrayList<>();
        _router.keySet().forEach((t) -> {
            if (t.startsWith("water.") == false) {
                _list.add(t);
            }
        });

        String[] topics = new String[_list.size()];
        _list.toArray(topics);

        messageSubscribeTopic(topics);
    }

    public void messageSubscribeTopic(String... topics) {
        try {
            messageSubscribeTopic(msg_receiver_url(), 0, topics);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean messageReceiveHandler(MessageM msg) throws Exception {
        XMessageHandler handler = _router.get(msg.topic);
        if (handler == null) {
            return true;
        } else {
            return handler.handler(msg);
        }
    }

    @Override
    public void cacheUpdateHandler(String tag) {
        super.cacheUpdateHandler(tag);

        if (tag.indexOf(".") > 0) {
            String[] ss = tag.split("\\.");
            if(ss.length ==2) {
                ICacheServiceEx cache = WeedConfig.libOfCache.get(ss[0]);

                if (cache != null) {
                    cache.clear(ss[1]);
                }
            }
        } else {
            //删掉cache
            for (ICacheServiceEx cache : WeedConfig.libOfCache.values()) {
                cache.clear(tag);
            }
        }
    }
}
