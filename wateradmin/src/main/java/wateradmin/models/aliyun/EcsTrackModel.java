package wateradmin.models.aliyun;

import lombok.Getter;

@Getter
public class EcsTrackModel {
    public String instanceId;
    public String name;
    public double cpu;
    public double memory;
    public double tcp;
    public double broadband;

    public double disk;

}
