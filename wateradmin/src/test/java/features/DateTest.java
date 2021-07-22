package features;

import org.junit.Test;
import org.noear.water.utils.Datetime;

import java.util.Date;

/**
 * @author noear 2021/6/9 created
 */
public class DateTest {
    @Test
    public void test(){
        System.out.println(Datetime.Now().toString("(yyyy-MM-dd HH:mm Z)"));

        System.out.println(new Date().getTime());
    }
}
