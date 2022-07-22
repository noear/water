package wateradmin.models.water_tool;

import lombok.Getter;
import org.noear.water.utils.Timespan;

import java.util.Date;

/**
 * @author noear 2022/7/22 created
 */
@Getter
public class CertificationModel {

    /**
     *
     */
    public long certification_id;
    /**
     * 分类标签
     */
    public String tag;
    /**
     * 唯一key
     */
    public String key;
    /**
     * 地址
     */
    public String url;
    /**
     * 备注
     */
    public String note;
    /**
     * 生效时间
     */
    public Date time_of_start;
    /**
     * 失效时间
     */
    public Date time_of_end;
    /**
     * 0:待检查；1检查中
     */
    public int state;
    /**
     * 检测异常数量
     */
    public int check_error_num;
    /**
     * 检测间隔时间(s)
     */
    public int check_interval;
    /**
     * 最后检查时间
     */
    public long check_last_time;
    /**
     * 最后检查状态（0：OK；1：error）
     */
    public int check_last_state;
    /**
     * 最后检查描述
     */
    public String check_last_note;
    /**
     * 是否为已启用
     */
    public int is_enabled;
    /**
     * 创建时间
     */
    public long gmt_create;
    /**
     * 最后修改时间
     */
    public long gmt_modified;

    public String remaining() {
        if (time_of_end == null) {
            return "";
        } else {
            return new Timespan(time_of_end, new Date()).days() + "天";
        }
    }
}