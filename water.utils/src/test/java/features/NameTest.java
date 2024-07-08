package features;

import org.junit.jupiter.api.Test;
import org.noear.water.utils.ClassUtils;

/**
 * @author noear 2021/3/25 created
 */
public class NameTest {
    @Test
    public void test(){
        System.out.println(ClassUtils.formatClassName("org.noear.water.utils.RandomUtils"));
        System.out.println(ClassUtils.formatClassName("org.junit.Test"));
        System.out.println(ClassUtils.formatClassName("org.noear.water.utils.ClassUtils"));
    }
}
