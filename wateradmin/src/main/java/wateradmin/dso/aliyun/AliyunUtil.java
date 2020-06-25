package wateradmin.dso.aliyun;

import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import wateradmin.models.water_cfg.ConfigModel;

import java.util.Properties;

public class AliyunUtil {
    public static IClientProfile getProfile(ConfigModel cfg){
        Properties prop = cfg.getProp();

        String regionId = prop.getProperty("regionId");
        String accessKeyId = prop.getProperty("accessKeyId");
        String secret = prop.getProperty("accessSecret");

        return DefaultProfile.getProfile(regionId, accessKeyId, secret);
    }
}
