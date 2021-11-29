package wateraide.models.data.water;

import lombok.Getter;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

@Table("water_cfg_logger")
@Getter
public class WaterCfgLoggerDo {
    /** 日志器ID */
    @PrimaryKey
    public Integer logger_id;
    /** 分组标签 */
    public String tag;
    /** 日志器 */
    public String logger;
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
    /** 创建时间 */
    public Long gmt_create;
    /** 最后修改时间 */
    public Long gmt_modified;
}