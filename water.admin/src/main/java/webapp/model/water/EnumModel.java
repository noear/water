package webapp.model.water;

import lombok.Getter;

/// <summary>
/// 生成:2017/12/20 04:06:41
/// 
/// </summary>
@Getter
public class EnumModel //implements IBinder
{
    public int enum_id;
    public String group;
    public String name;
    public int value;

//    @Override
//	public void bind(GetHandlerEx s)
//	{
//		//1.source:数据源
//		//
//        enum_id = s.get("enum_id").value(0);
//        group = s.get("group").value(null);
//        name = s.get("name").value(null);
//        value = s.get("value").value(0);
//	}
//
//    @Override
//	public IBinder clone()
//	{
//		return new EnumModel();
//	}
}