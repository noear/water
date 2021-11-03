package tool;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.Props;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.weed.DbContext;
import org.noear.weed.SelectQ;
import watersetup.Config;
import watersetup.models.water.WaterToolMonitorModel;
import watersetup.models.water.WaterToolReportModel;
import watersetup.models.water.WaterToolSynchronousModel;
import watersetup.models.water_bcf.BcfConfigModel;
import watersetup.models.water_bcf.BcfGroupModel;
import watersetup.models.water_bcf.BcfResourceModel;
import watersetup.models.water_bcf.BcfUserModel;
import watersetup.models.water_cfg.BrokerModel;
import watersetup.models.water_cfg.ConfigModel;
import watersetup.models.water_cfg.LoggerModel;
import watersetup.models.water_cfg.WhitelistModel;
import watersetup.models.water_paas.*;

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
public class json {
    @Test
    public void buildJosn() throws Exception {
        Props waterProp = Solon.cfg().getProp("water.ds");
        //Props waterBcfProp = Solon.cfg().getProp("water.ds");
        Props waterPaasProp = Solon.cfg().getProp("water.ds");
        waterPaasProp.setProperty("schema", "water_paas");

        Config.checkProp(waterProp);
        DbContext waterDb = new DbContext(waterProp);

        Config.checkProp(waterPaasProp);
        DbContext waterPaasDb = new DbContext(waterPaasProp);


        //water
        buildTableJosn(waterDb, "water_cfg_broker", "water");

        buildTableJosn(waterDb, "water_cfg_logger", "water");
        buildTableJosn(waterDb, "water_cfg_properties", "water");
        buildTableJosn(waterDb, "water_cfg_whitelist", "water");

        buildTableJosn(waterDb, "water_tool_monitor", "water");
        buildTableJosn(waterDb, "water_tool_report", "water");
        buildTableJosn(waterDb, "water_tool_synchronous", "water");

        //water_bcf
        buildTableJosn(waterDb, "bcf_config", "water_bcf");
        buildTableJosn(waterDb, "bcf_group", "water_bcf");
        buildTableJosn(waterDb, "bcf_resource", "water_bcf");
        buildTableJosn(waterDb, "bcf_resource_linked", "water_bcf");

        buildTableJosn(waterDb, "bcf_user", "water_bcf");
        buildTableJosn(waterDb, "bcf_user_linked", "water_bcf");


        //water_paas
        buildTableJosn(waterPaasDb, "paas_file", "water_paas");

        buildTableJosn(waterPaasDb, "rubber_actor", "water_paas");
        buildTableJosn(waterPaasDb, "rubber_block", "water_paas");

        buildTableJosn(waterPaasDb, "rubber_model", "water_paas");
        buildTableJosn(waterPaasDb, "rubber_model_field", "water_paas");
        buildTableJosn(waterPaasDb, "rubber_scheme", "water_paas");
        buildTableJosn(waterPaasDb, "rubber_scheme_node", "water_paas");
        buildTableJosn(waterPaasDb, "rubber_scheme_node_design", "water_paas");
        buildTableJosn(waterPaasDb, "rubber_scheme_rule", "water_paas");
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
