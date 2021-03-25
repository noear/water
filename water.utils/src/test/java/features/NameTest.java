package features;

import org.junit.Test;
import org.noear.water.utils.NameUtils;

/**
 * @author noear 2021/3/25 created
 */
public class NameTest {
    @Test
    public void test(){
        System.out.println(NameUtils.formatClassName("org.noear.water.utils.RandomUtils"));
        System.out.println(NameUtils.formatClassName("org.junit.Test"));
        System.out.println(NameUtils.formatClassName("org.noear.water.utils.ClassUtils"));
    }
}
