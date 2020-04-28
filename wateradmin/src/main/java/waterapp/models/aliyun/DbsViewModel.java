package waterapp.models.aliyun;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DbsViewModel {

    public String version;
    public String zoneId;
    public String dBInstanceClass;
    public long maxCon;
    public int maxIOPS;
    public String instanceId;
    public String name;
    public String bandWith;//带宽
    public String qps;
    public long capacity; //容量
    public int type;
    public String networkType;

    public String DBCategory;//分类
    public String DBInstanceCPU;
    public int DBInstanceStorage;
    public long DBInstanceMemory;

    //https://help.aliyun.com/document_detail/26231.html?spm=a2c4g.11186623.2.33.7fd57019AbZl9L

    public String productType() {
        if (type > 0) {
            return (capacity / 1024) + "G/" + (bandWith) + "MB";
        } else {
            return DBInstanceCPU + "核/" + (DBInstanceMemory/1024) + "G/" + DBInstanceStorage + "G";
        }
    }
}
