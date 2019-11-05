package webapp.model.water_paas;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PaasPullModel {

    /**  */
    public int pull_id;
    /** 计划类型 */
    public int pull_type;
    /** 分类标签 */
    public String tag;
    /** 计划名称 */
    public String pull_name;
    /** 数据源 */
    public String source;
    /** jtsql代码 */
    public String target_dir;
    public String target_url;
    /** 0初始状态；1等待处理；2处理中；8处理失败；9处理成功 */
    public int state;
    /** 最后处理时间 */
    public Date last_fulltime;
    /** 是否启用 */
    public int is_enabled;
    /**  */
    public String alarm_mobile;
    /**  */
    public String alarm_sign;

    public String state_str() {
        switch (state) {
            case 1:
                return "等待拉取";
            case 2:
                return "拉取中..";
            case 8:
                return "拉取失败";
            case 9:
                return "拉取成功";
            default:
                return "/";
        }
    }

}