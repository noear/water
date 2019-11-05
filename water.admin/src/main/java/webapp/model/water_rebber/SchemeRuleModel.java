package webapp.model.water_rebber;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

import java.util.Date;

/// <summary>
/// 生成:2018/05/15 10:57:40
/// 
/// </summary>
@Getter
public class SchemeRuleModel implements IBinder
{
    public int rule_id;
    public int scheme_id;
    public String group;
    public String name_display;
    public int advice;
    public int score;
    public int sort;
    public String expr;
    public String expr_display;
    public Date last_updatetime;
    public int is_enabled;
    public int counts;

	public void bind(GetHandlerEx s)
	{
		//1.source:数据源
		//
        rule_id = s.get("rule_id").value(0);
        scheme_id = s.get("scheme_id").value(0);
        group = s.get("group").value(null);
        name_display = s.get("name_display").value(null);
        advice = s.get("advice").value(0);
        score = s.get("score").value(0);
        sort = s.get("sort").value(0);
        expr = s.get("expr").value(null);
        expr_display = s.get("expr_display").value(null);
        is_enabled = s.get("is_enabled").value(0);
        counts = s.get("counts").value(0);
        last_updatetime = s.get("last_updatetime").value(null);
	}
	
	public IBinder clone()
	{
		return new SchemeRuleModel();
	}
}