package wateradmin.models.water_reg;

import lombok.Getter;
import org.noear.weed.*;

/// <summary>
/// 生成:2018/04/26 09:46:37
/// 
/// </summary>
@Getter
public class ServiceSpeedHourModel implements IBinder
{
    public int row_id;
    public String service;
    public String tag;
    public String name;
    public long average;
    public long fastest;
    public long slowest;
    public long total_num;
    public long total_num_slow1;
    public long total_num_slow2;
    public long total_num_slow5;
    public int log_date;
    public int log_hour;

    public long val;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        row_id = s.get("row_id").value(0);
        service = s.get("service").value(null);
        tag = s.get("tag").value(null);
        name = s.get("name").value(null);
        average = s.get("average").value(0L);
        fastest = s.get("fastest").value(0L);
        slowest = s.get("slowest").value(0L);
        total_num = s.get("total_num").value(0L);
        total_num_slow1 = s.get("total_num_slow1").value(0L);
        total_num_slow2 = s.get("total_num_slow2").value(0L);
        total_num_slow5 = s.get("total_num_slow5").value(0L);
        log_date = s.get("log_date").value(0);
        log_hour = s.get("log_hour").value(0);

        val = s.get("val").value(0L);
	}
	
	public IBinder clone()
	{
		return new ServiceSpeedHourModel();
	}
}