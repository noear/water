package features;

import org.junit.Test;
import org.noear.snack.ONode;
import org.noear.water.model.ConfigM;
import org.noear.water.utils.GzipUtils;
import org.noear.water.utils.JsondEntity;
import org.noear.water.utils.JsondUtils;

/**
 * @author noear 2021/1/20 created
 */
public class JsondTest {
    @Test
    public void test1() throws Exception {
        ConfigM cfg = new ConfigM("water", "water.url=http://xxx/xx\nwater.name=noear", 12);

        String jsond = JsondUtils.encode("cfg", cfg);

        JsondEntity entity = JsondUtils.decode(jsond);

        assert "cfg".equals(entity.table);
    }

    @Test
    public void test2() throws Exception {
        ConfigM cfg = new ConfigM("water", "water.url=http://xxx/xx\nwater.name=noear", 12);
        String json = ONode.stringify(cfg);

        String gzip = GzipUtils.gZip(json);

        String json2 = GzipUtils.unGZip(gzip);

        assert json2 != null;
    }
}
