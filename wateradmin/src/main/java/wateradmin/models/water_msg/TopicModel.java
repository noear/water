package wateradmin.models.water_msg;

import lombok.Getter;
import org.noear.water.utils.EncryptUtils;

/// <summary>
/// 生成:2017/12/21 04:30:35
/// 
/// </summary>
@Getter
public class TopicModel
{
    public int topic_id;
    public String topic_name;
    public String tag;
    public int max_msg_num;
    public int max_distribution_num;
    public int max_concurrency_num;
    public int stat_msg_day_num;
    public int alarm_model;

	public String topic_md5(){
        return "%7Bmd5%7D" + EncryptUtils.md5(topic_name);
    }
}