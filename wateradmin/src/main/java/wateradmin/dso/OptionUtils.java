package wateradmin.dso;

import org.noear.solon.Solon;
import wateradmin.models.ScaleType;

/**
 * @author noear 2021/11/20 created
 */
public class OptionUtils {
    /**
     * 服务注册数量规模
     * <p>
     * 参数：0上，1中，2大，3超大
     */
    public static ScaleType serviceScale() {
        int scale = Solon.cfg().getInt("water.option.service.scale", 0);

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
     * 参数：0上，1中，2大，3超大
     */
    public static ScaleType topicScale() {
        int scale = Solon.cfg().getInt("water.option.topic.scale", 0);

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
