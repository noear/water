package org.noear.water.client_solon;

import org.noear.bcf.BcfClient;
import org.noear.bcf.models.BcfUserModel;
import org.noear.solon.core.XContext;

public abstract class XWaterSessionBcf  {
    private static XWaterSessionBcf _global;
    public static XWaterSessionBcf global(){
        return _global;
    }

    public XWaterSessionBcf(){
        super();
        _global = this;
    }

    public XContext context() {
        return XContext.current();
    }

    protected <T> T doGet(String key, T def){
        Object tmp = context().session(key);
        if(tmp == null){
            return def;
        }else{
            return (T)tmp;
        }
    }

    protected void doSet(String key,Object val){
        context().sessionSet(key,val);
    }

    protected boolean doHas(String key) {
        return context().session(key) != null;
    }

    public abstract String service();

    public abstract void loadModel(BcfUserModel model) throws Exception;

    private int doGetPuid(){
        return doGet("puid", 0);
    }

    public final int getPUID() {
        int temp = doGetPuid();

        if (temp > 0 && hasReload()) {
            try {
                BcfUserModel user = BcfClient.login(temp);
                loadModel(user);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return temp;
    }

    public final void setPUID(int puid) {
        doSet("puid", puid);
        set("puid", puid);
    }

    public final String getUserId() {
        return doGet("user_id", null);
    }

    public final void setUserId(String user_id) {
        doSet("user_id", user_id);
    }

    public final String getUserName() {
        return doGet("user_name", null);
    }

    public final void setUserName(String user_name) {
        doSet("user_name", user_name);
    }

    /////////////////////////////////////////////////
    private boolean hasReload() {
        return doGetPuid() != servicePUID();
    }

    public boolean has(String key) {
        return doHas(service() + "_" + key);
    }

    private int servicePUID() {
        return get("puid", 0);
    }

    public void set(String key, Object val) {
        doSet(service() + "_" + key, val);
    }

    public <T> T get(String key, T def) {
        return doGet(service() + "_" + key, def);
    }




    protected String stateGet(String key) {
        return context().attr(key, null);
    }

    protected void stateSet(String key, String val) {
        context().attrSet(key, val);
    }

    protected boolean stateHas(String key) {
        return context().attr(key, null) != null;
    }
}
