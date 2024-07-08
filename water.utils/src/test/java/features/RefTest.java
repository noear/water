package features;

import org.junit.jupiter.api.Test;
import org.noear.water.model.LogM;

/**
 * @author noear 2021/11/1 created
 */
public class RefTest {
    @Test
    public void test1() {
        LogM m1 = new LogM();
        m1.content = "";

        LogM m2 = m1;

        LogM m3 = new LogM();
        m3.content = "";

        assert m1 == m2;
        assert m1.equals(m2);

        assert m1 != m3;
        assert m1.equals(m3) == false;
    }
}
