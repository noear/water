package org.noear.water.dubbo.solon.plugin;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.Registry;
import org.noear.snack.ONode;
import org.noear.water.WaterClient;
import org.noear.water.model.DiscoverM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if ("consumer".equals(url.getProtocol())) {
            listener.notify(lookup(url));
        }
    }

    @Override
    public void unsubscribe(URL url, NotifyListener listener) {
        if ("consumer".equals(url.getProtocol())) {
            listener.notify(lookup(url));
        }
    }

    @Override
    public List<URL> lookup(URL url) {
        String service_key = RegistryUtils.buildServiceKey(url);

        String consumer = WaterClient.localService();
        String consumer_address = WaterClient.localHost();

        DiscoverM discoverM = WaterClient.Registry.discover(service_key, consumer, consumer_address);

        List<URL> list = new ArrayList<>();

        if (discoverM != null) {
            discoverM.list.forEach(m1 -> {
                URL tmp = RegistryUtils.buildUrl(m1);

                list.add(tmp);
            });
        }

        return list;
    }
}
