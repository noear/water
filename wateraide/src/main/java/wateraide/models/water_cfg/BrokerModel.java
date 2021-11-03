package wateraide.models.water_cfg;

import lombok.Getter;
import org.noear.water.protocol.model.message.BrokerMeta;
import org.noear.water.utils.EncryptUtils;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

@Table("water_cfg_broker")
@Getter
public class BrokerModel implements BrokerMeta {
    /** 协调器ID */
    @PrimaryKey
    public Integer broker_id;
    /** 分组标签 */
    public String tag;
    /** 协调器 */
    public String broker;
    /** 累积行数 */
    public Long row_num;
    /** 今日行数 */
    public Long row_num_today;
    /** 今日错误行数 */
    public Long row_num_today_error;
    /** 昨天行数 */
    public Long row_num_yesterday;
    /** 昨天错误行数 */
    public Long row_num_yesterday_error;
    /** 前天行数 */
    public Long row_num_beforeday;
    /** 前天错误行数 */
    public Long row_num_beforeday_error;
    /** 保留天数 */
    public Integer keep_days;
    /** 数据源 */
    public String source;
    /** 备注 */
    public String note;
    /** 是否启用 */
    public Integer is_enabled;
    /** 是否报警 */
    public Integer is_alarm;
    /** 更新时间 */
    public Date update_fulltime;

    public boolean isHighlight() {
        return (row_num_today_error > 0);
    }

    @Override
    public int getKeepDays() {
        return keep_days;
    }

    public String broker_md5() {
        return "%7Bmd5%7D" + EncryptUtils.md5(broker);
    }
}