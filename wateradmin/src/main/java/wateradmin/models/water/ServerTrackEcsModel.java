package wateradmin.models.water;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;
import wateradmin.dso.IaasTypeUtils;

import java.util.Date;

/// <summary>
/// 生成:2017/12/28 11:55:12
/// 
/// </summary>
@Getter
public class ServerTrackEcsModel implements IBinder {
    public int server_id;
    public String tag;
    public String name;
    public String iaas_key;
    public int iaas_type;
    public String iaas_attrs;

    public double cpu_usage;
    public double memory_usage;
    public double disk_usage;
    public double broadband_usage;
    public long tcp_num;
    public int sev_num;
    public String address_local;

    public Date gmt_modified;

    public void bind(GetHandlerEx s) {
        //1.source:数据源
        //
        server_id = s.get("server_id").value(0);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        iaas_key = s.get("iaas_key").value(null);
        iaas_type = s.get("iaas_type").value(0);
        iaas_attrs = s.get("iaas_attrs").value(null);

        cpu_usage = s.get("cpu_usage").value(0.0d);
        memory_usage = s.get("memory_usage").value(0.0d);
        disk_usage = s.get("disk_usage").value(0.0d);
        broadband_usage = s.get("broadband_usage").value(0.0d);
        tcp_num = s.get("tcp_num").value(0l);

        sev_num = s.get("sev_num").value(0);

        address_local = s.get("address_local").value(null);

        gmt_modified = s.get("gmt_modified").dateValue(null);
        if (gmt_modified == null) {
            gmt_modified = new Date();
        }
    }

    public IBinder clone() {
        return new ServerTrackEcsModel();
    }

    public String iaas_type_str() {
        return IaasTypeUtils.iaas_type_str(iaas_type);
    }
}