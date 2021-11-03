package wateraide.models.water_paas;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

/**
 * @author noear 2021/11/3 created
 */
@Table("rubber_scheme_node")
public class RubberSchemeNodeModel {

    /**  */
    @PrimaryKey
    public Integer node_id;
    /**  */
    public Integer scheme_id;
    /**  */
    public String node_key;
    /** 节点类型：0开始，1线，2执行节点，3排他网关，4并行网关，5汇聚网关，9结束 */
    public Integer type;
    /** 代号 */
    public String name;
    /** 上个节点ID（type=line时，才有值 ） */
    public String prve_key;
    /** 下个节点ID（type=line时，才有值 ） */
    public String next_key;
    /** 分支条件（type=line时，才有值 ：left,op,right,ct；left,op,right,ct） */
    public String condition;
    /** 执行任务（type=exec时，才有值：F,tag/fun1；R,tag/rule1 ） */
    public String tasks;
    /** 参与者（type=exec时，才有值 ：tag/name） */
    public String actor;
    /**  */
    public String actor_display;
    /** 最后更新时间 */
    public Date last_updatetime;
    /** 是否启用  0：未启用  1：启用  */
    public Integer is_enabled;
}
