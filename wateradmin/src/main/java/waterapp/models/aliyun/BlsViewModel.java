package waterapp.models.aliyun;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlsViewModel {

    public String instanceId;
    public String regionId;
    public String instanceName;
    public String loadBalancerSpec;
    public String maxCon;
    public String cps;
    public String qps;

    public void setLoadBalancerSpec(String loadBalancerSpec) {
        switch (loadBalancerSpec){
            case "slb.s1.small" :
                maxCon="5,000";
                cps="3,000";
                qps="1,000";
                break;
            case "slb.s2.small" :
                maxCon="50,000";
                cps="5,000";
                qps="5,000";
                break;
            case "slb.s2.medium" :
                maxCon="100,000";
                cps="10,000";
                qps="10,000";
                break;
            case "slb.s3.small" :
                maxCon="200,000";
                cps="20,000";
                qps="20,000";
                break;
            case "slb.s3.medium" :
                maxCon="500,000";
                cps="50,000";
                qps="30,000";
                break;
            case "slb.s3.large" :
                maxCon="1,000,000";
                cps="100,000";
                qps="50,000";
                break;
        }
        this.loadBalancerSpec = loadBalancerSpec;
    }
}
