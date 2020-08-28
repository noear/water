package org.noear.water.dubbo.solon.plugin;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.Registry;
import org.noear.water.WaterClient;
import org.noear.water.model.DiscoverM;
import org.noear.water.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class WaterRegistry implements Registry {
    private URL url;

    public WaterRegistry(URL url) {
        this.url = url;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void register(URL url) {
        WaterRegistryLib.register(url);
    }

    @Override
    public void unregister(URL url) {
        WaterRegistryLib.unregister(url);
    }

    @Override
    public void subscribe(URL url, NotifyListener listener) {
        if("consumer".equals(url.getProtocol())){
            listener.notify(lookup(url));
        }
    }

    @Override
    public void unsubscribe(URL url, NotifyListener listener) {
        if("consumer".equals(url.getProtocol())){
            listener.notify(lookup(url));
        }
    }

    @Override
    public List<URL> lookup(URL url) {
        String service = url.getParameter("interface");

        DiscoverM discoverM = WaterClient.Registry.discover(service, "", "");

        List<URL> list = new ArrayList<>();

        if(discoverM != null) {
            discoverM.list.forEach(m1 -> {
                list.add(URL.valueOf(m1.address));
            });
        }

        return list;
    }
}
