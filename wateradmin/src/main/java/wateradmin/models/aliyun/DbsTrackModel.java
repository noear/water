package wateradmin.models.aliyun;

import lombok.Getter;

@Getter
public class DbsTrackModel {
    public String instanceId;
    public String name;
    public double connect_usage;
    public double cpu_usage;
    public double memory_usage;
    public double disk_usage;

    public double disk;

}
