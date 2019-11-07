package webapp.models.water;

import lombok.Getter;
import org.noear.weed.*;
import java.util.*;

/// <summary>
/// 生成:2017/12/20 02:44:02
/// 
/// </summary>
@Getter
public class MonitorModel
{
    public int id;
    public String key;
    public String name;
    public int type;
    public String source;
    public String source_model;
    public String rule;
    public String task_tag;
    public String task_tag_exp;
    public String alarm_mobile;
    public String alarm_exp;
    public int alarm_count;
    public String alarm_sign;
    public int is_enabled;
    public String tag;
    public long counts;

	public String type_str(){
	    if(type==1){
	        return "数据简报";
        }else{
	        return "数据预警";
        }
    }
}