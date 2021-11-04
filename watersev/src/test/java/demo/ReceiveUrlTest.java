package demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;
import watersev.WatersevApp;
import watersev.dso.MsgUtils;

/**
 * @author noear 2021/11/4 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
@SolonTest(WatersevApp.class)
public class ReceiveUrlTest {

    @Test
    public void  test(){
        String url;

        url = MsgUtils.getReceiveUrl2("@wateradmin/msg/reg");
        System.out.println(url);

        url = MsgUtils.getReceiveUrl2("@wateradmin");
        System.out.println(url);

        url = MsgUtils.getReceiveUrl2("http://xxx/path/msg/reg");
        System.out.println(url);
    }
}
