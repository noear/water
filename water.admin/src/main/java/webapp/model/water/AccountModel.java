package webapp.model.water;

import lombok.Getter;

/// <summary>
/// 生成:2017/12/20 04:06:41
/// 
/// </summary>
@Getter
public class AccountModel //implements IBinder
{
    public int account_id;
    public String name;
    public String alarm_mobile;
    public String access_id;
    public String access_key;
    public int is_admin;
    public String note;

//	public void bind(GetHandlerEx s)
//	{
//		//1.source:数据源
//		//
//        account_id = s.get("account_id").value(0);
//        name = s.get("name").value(null);
//        alarm_mobile = s.get("alarm_mobile").value(null);
//        access_id = s.get("access_id").value(null);
//        access_key = s.get("access_key").value(null);
//        is_admin = s.get("is_admin").value(0);
//        note = s.get("note").value(null);
//	}
//
//	public IBinder clone()
//	{
//		return new AccountModel();
//	}
}