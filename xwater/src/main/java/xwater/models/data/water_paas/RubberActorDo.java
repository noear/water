package xwater.models.data.water_paas;

import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;

/**
 * @author noear 2021/11/3 created
 */
@Table("rubber_actor")
public class RubberActorDo {
    /** 参与ID */
    @PrimaryKey
    public Integer actor_id;
    /** 分类标签 */
    public String tag;
    /** 参与者代号 */
    public String name;
    /** 参与者显示名 */
    public String name_display;
    /** 备注 */
    public String note;
    /** 最后更新时间 */
    public Long last_updatetime;


}
