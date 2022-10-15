package xwater.models.data.grit;

import lombok.Getter;
import org.noear.wood.annotation.PrimaryKey;
import org.noear.wood.annotation.Table;

import java.io.Serializable;

/**
 * 资源连接（连接主体）
 *
 * @author noear
 * @since 1.0
 */
@Table("grit_resource_linked")
@Getter
public class GritResourceLinkedDo implements Serializable {
    /** 连接ID */
    @PrimaryKey
    public Long link_id;
    /** 资源ID */
    public Long resource_id;
    /** 主体ID */
    public Long subject_id;
    /** 主体类型 */
    public Integer subject_type;
    /** 创建时间 */
    public Long gmt_create;
}
