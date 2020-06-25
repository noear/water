package wateradmin.models.aliyun;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AliyunResponse {

    public  String instanceId;

    public String port;

    public String diskname; //仅在空间应用时有值

    public double maximum;

    public  double minimum;

    public double average;

    public String userId;

    public  long timestamp;

    public String state;
}
