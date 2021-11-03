package watersetup.models.water_paas;

import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.util.Date;

/**
 * @author noear 2021/11/3 created
 */
@Table("rubber_block")
public class RubberBlockModel {

    /**  */
    @PrimaryKey
    public int block_id;
    /** 分类标签 */
    public String tag;
    /** 代号 */
    public String name;
    /** 显示名 */
    public String name_display;
    /**  */
    public String note;
    /** 相关数据(sponge/angel) */
    public String related_db;
    /** 相关数据表 */
    public String related_tb;
    /**  */
    public int is_editable;
    /**  */
    public int is_enabled;
    /** 数据结构({f1:'xx'}) */
    public String struct;
    /** 应用表达式 */
    public String app_expr;
    /**  */
    public Date last_updatetime;
}
