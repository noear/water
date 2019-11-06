package org.noear.water.admin.plugin_aliyun.dso;

import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.noear.water.admin.plugin_aliyun.model.water.ConfigModel;

import java.util.Properties;

public class AliyunUtil {
    public static IClientProfile getProfile(ConfigModel cfg){
        Properties prop = cfg.toProp();

        String regionId = prop.getProperty("regionId");
        String accessKeyId = prop.getProperty("accessKeyId");
        String secret = prop.getProperty("secret");

        return DefaultProfile.getProfile(regionId, accessKeyId, secret);
    }
}
