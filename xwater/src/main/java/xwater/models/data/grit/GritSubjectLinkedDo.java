package xwater.models.data.grit;

import lombok.Getter;
import org.noear.weed.annotation.PrimaryKey;
import org.noear.weed.annotation.Table;

import java.io.Serializable;

/**
 * 主体连接（连接自己）
 *
 * @author noear
 * @since 1.0
 */
@Table("grit_subject_linked")
@Getter
public class GritSubjectLinkedDo implements Serializable {
    /** 连接ID */
    @PrimaryKey
    public Long link_id;
    /** 主体ID */
    public Long subject_id;
    /** 分组主体ID */
    public Long group_subject_id;
    /** 创建时间 */
    public Long gmt_create;
}
