package webapp.controller.dev;

import org.noear.water.admin.tools.controller.BaseController;
import org.noear.water.admin.tools.viewModels.ViewModel;
import org.noear.water.client.WaterClient;
import org.noear.water.tools.TextUtils;
import org.noear.weed.DbContext;

import org.noear.solon.annotation.XMapping;

import org.noear.solon.annotation.XController;
import org.noear.solon.core.ModelAndView;
import webapp.dao.BcfTagChecker;
import webapp.dao.db.DbWaterApi;
import webapp.models.water.ConfigModel;
import java.sql.SQLException;
import java.util.List;


@XController
@XMapping("dev/ddl")
public class DDLController extends BaseController {

    private static final int CFG_TYPE_DB = 10;

    private static final String SEPARATOR_BETWEEN_TAG_AND_KEY = ".";

    private static final String DDL_COLUMN_NAME = "Create Table";

    private static final String SQL_GET_TABLES() {
        return "SHOW TABLES;";
    }

    private static String buildSqlGetDDL(String table) {
        return "SHOW CREATE TABLE `" + table + "`;";
    }

    @XMapping("")
    public ModelAndView ddl(String tag_name) throws SQLException {
        List<ConfigModel> resp = DbWaterApi.getTagGroupWithType(CFG_TYPE_DB);

        BcfTagChecker.filter(resp, m -> m.tag);

        if (!TextUtils.isEmpty(tag_name)) {
            viewModel.put("tag_name", tag_name);
        } else {
            if (!resp.isEmpty()) {
                viewModel.put("tag_name", resp.get(0).tag);
            } else {
                viewModel.put("tag_name", null);
            }
        }
        viewModel.put("resp", resp);

        return view("dev/ddl");
    }

    @XMapping("inner/{tagname}")
    public ModelAndView inner(String tagname) throws SQLException {

        List<ConfigModel> cfgs = DbWaterApi.getConfigByType(tagname, CFG_TYPE_DB);

        viewModel.put("cfgs", cfgs);

        viewModel.put("tag_name", tagname);

        return view("dev/ddl_inner");
    }

    @XMapping("ajax/tb")
    public ViewModel tb(String tag,
                        String key) throws SQLException {

        DbContext db = WaterClient.Config.getByTagKey(tag + SEPARATOR_BETWEEN_TAG_AND_KEY + key).getDb();

        List<String> tbs = db.sql(SQL_GET_TABLES()).getDataList().toArray(0);

        return new ViewModel().code(1, "成功").set("tbs", tbs);

    }

    @XMapping("ajax/getddl")
    public ViewModel get(String tag,
                         String key,
                         String tb) throws SQLException {

        DbContext db = WaterClient.Config.getByTagKey(tag + SEPARATOR_BETWEEN_TAG_AND_KEY + key).getDb();

        String ddl = db.sql(buildSqlGetDDL(tb)).getDataItem().getString(DDL_COLUMN_NAME);

        return new ViewModel().code(1, "成功").set("ddl", ddl);

    }

}
