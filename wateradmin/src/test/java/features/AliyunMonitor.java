package features;

import org.junit.Test;

import java.util.Arrays;

public class AliyunMonitor {
    @Test
    public void test(){
        String[] tag = "water.update.cache".split("\\.|_");

        System.out.println(Arrays.asList(tag));

        tag = "water_update_cache".split("\\.|_");

        System.out.println(Arrays.asList(tag));
    }

    private String aliyunApi(String iaasType,String iaasKey){
        String url = "https://ecs.aliyuncs.com/?Action=CreateSnapshot";

        return "";
    }
}
