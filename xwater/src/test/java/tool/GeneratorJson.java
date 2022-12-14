package tool;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.Props;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.wood.DbContext;
import xwater.Config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author noear 2021/11/3 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class GeneratorJson {
    @Test
    public void generate() throws Exception {
        Props waterProp = Solon.cfg().getProp("water.ds");

        Props waterPaasProp = Solon.cfg().getProp("water.ds");
        waterPaasProp.setProperty("schema", "water_paas");

        Config.checkProp(waterProp);
        DbContext waterDb = new DbContext(waterProp);

        Config.checkProp(waterPaasProp);
        DbContext waterPaasDb = new DbContext(waterPaasProp);


        //water
//        buildTableJosn(waterDb, "water_cfg_broker", "water");
//        buildTableJosn(waterDb, "water_cfg_gateway", "water");
//        buildTableJosn(waterDb, "water_cfg_logger", "water");
//        buildTableJosn(waterDb, "water_cfg_properties", "water");
//        buildTableJosn(waterDb, "water_cfg_whitelist", "water");

//        buildTableJosn(waterDb, "water_tool_monitor", "water");
//        buildTableJosn(waterDb, "water_tool_report", "water");
//        buildTableJosn(waterDb, "water_tool_synchronous", "water");

        //grit
        buildTableJosn(waterDb, "grit_resource", "grit");
        buildTableJosn(waterDb, "grit_resource_linked", "grit");
//        buildTableJosn(waterDb, "grit_subject", "grit");
//        buildTableJosn(waterDb, "grit_subject_linked", "grit");



        //water_paas
        buildTableJosn(waterPaasDb, "luffy_file", "water_paas");

//        buildTableJosn(waterPaasDb, "rubber_block", "water_paas");

//        buildTableJosn(waterPaasDb, "rubber_model", "water_paas");
//        buildTableJosn(waterPaasDb, "rubber_model_field", "water_paas");
//        buildTableJosn(waterPaasDb, "rubber_scheme", "water_paas");
//        buildTableJosn(waterPaasDb, "rubber_scheme_node", "water_paas");
//        buildTableJosn(waterPaasDb, "rubber_scheme_node_design", "water_paas");
//        buildTableJosn(waterPaasDb, "rubber_scheme_rule", "water_paas");
    }

    private void buildTableJosn(DbContext db, String table, String schema) throws Exception {
        List<Map<String, Object>> mapList = db.table(table).selectMapList("*");
        String json = ONode.stringify(mapList);

        String fileName =  schema + "_" + table + ".json";

        System.out.println(">>>>>: " + fileName);

        fileName = "/Users/noear/Downloads/water_init_json/"+fileName;

        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        Utils.transferTo(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)), new FileOutputStream(file));
    }
}
