package org.noear.water.dubbo.solon.plugin;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.Registry;
import org.noear.water.WaterClient;

import java.util.List;

public class WaterRegistry implements Registry {
    private URL url;
    public WaterRegistry(URL url){
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
        WaterClient.Registry.register("",url.getAddress(),"",null,false);
    }

    @Override
    public void unregister(URL url) {
        //WaterClient.Registry.set("", url.getAddress());
    }

    @Override
    public void subscribe(URL url, NotifyListener listener) {

    }

    @Override
    public void unsubscribe(URL url, NotifyListener listener) {

    }

    @Override
    public List<URL> lookup(URL url) {
        return null;
    }
}
