package wateradmin.dso;

import org.noear.solon.Solon;
import wateradmin.models.ScaleType;

/**
 * @author noear 2021/11/20 created
 */
public class SettingUtils {
    public static final String name_topic = "water.setting.scale.topic";
    public static final String name_service = "water.setting.scale.service";
    public static final String name_subject = "water.setting.scale.subject";

    /**
     * 服务注册数量规模
     * <p>
     * 参数：0小，1中，2大
     */
    public static ScaleType serviceScale() {
        int scale = Solon.cfg().getInt(name_service, 0);

        switch (scale) {
            case 1:
                return ScaleType.medium;//分组 + 详情
            case 2:
                return ScaleType.large;//只有列表，不管详情
            default:
                return ScaleType.small; //一个页面，显示详情
        }
    }

    /**
     * 消息主题数量规模
     * <p>
     * 参数：0少，1多
     */
    public static ScaleType topicScale() {
        int scale = Solon.cfg().getInt(name_topic, 0);

        switch (scale) {
            case 1:
                return ScaleType.medium;//分组 + 详情
            case 2:
                return ScaleType.large;//只有列表，不管详情
            default:
                return ScaleType.small; //一个页面，显示详情
        }
    }

    /**
     * 系统主体数量规模（用户）
     * <p>
     * 参数：0少，1多
     */
    public static ScaleType subjectScale() {
        int scale = Solon.cfg().getInt(name_subject, 0);

        switch (scale) {
            case 1:
                return ScaleType.medium;//分组 + 详情
            case 2:
                return ScaleType.large;//只有列表，不管详情
            default:
                return ScaleType.small; //一个页面，显示详情
        }
    }
}
