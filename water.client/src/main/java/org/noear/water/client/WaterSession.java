package org.noear.water.client;

import org.noear.water.tools.EncryptUtils;
import org.noear.water.tools.IDUtil;
import org.noear.water.tools.RedisX;
import org.noear.water.tools.TextUtils;

public abstract class WaterSession {
    public final static String SESSIONID_KEY = WaterClient.Config.get("water","cookie_key").value;
    public final static String SESSIONID_MD5(){return SESSIONID_KEY+"2";}
    public final static String SESSIONID_encrypt = "&L8e!@T0";

    public abstract int expiry();//过期秒数
    public abstract String domain();//COOKIE域名
    public abstract String cookieGet(String key);
    public abstract void   cookieSet(String key, String val);


    private final RedisX redisX;
    public WaterSession(){
        redisX = WaterClient.Config.get("water","water_redis").getRd(6);
    }


    public String sessionID(){
        String skey = cookieGet(SESSIONID_KEY);
        String smd5 = cookieGet(SESSIONID_MD5());

        if(TextUtils.isEmpty(skey)==false && TextUtils.isEmpty(smd5)==false) {
            if (EncryptUtils.md5(skey + SESSIONID_encrypt).equals(smd5)) {
                return skey;
            }
        }

        skey = IDUtil.guid();
        cookieSet(SESSIONID_KEY,skey);
        cookieSet(SESSIONID_MD5(), EncryptUtils.md5(skey + SESSIONID_encrypt));
        return skey;
    }

    protected void updateSessionID() {
        String skey = cookieGet(SESSIONID_KEY);

        if (TextUtils.isEmpty(skey) == false) {
            cookieSet(SESSIONID_KEY, skey);
            cookieSet(SESSIONID_MD5(), EncryptUtils.md5(skey + SESSIONID_encrypt));

            doDelay();
        }
    }

    protected void doDelay(){
        redisX.open0((ru)->ru.key(sessionID()).expire(expiry()).delay());
    }

    protected void doSet(String key, Object val){
        redisX.open0((ru)->ru.key(sessionID()).expire(expiry()).hashSet(key,String.valueOf(val)));
    }

    protected String doGet(String key, String def) {
        String temp = redisX.open1((ru) -> ru.key(sessionID()).expire(expiry()).hashGet(key));
        if(TextUtils.isEmpty(temp)){
            return def;
        }else{
            return temp;
        }
    }

    protected boolean doHas(String key){
        return redisX.open1((ru) -> ru.key(sessionID()).hashHas(key));
    }


    public void clear(){
        redisX.open0((ru)->ru.key(sessionID()).delete());
    }
}
